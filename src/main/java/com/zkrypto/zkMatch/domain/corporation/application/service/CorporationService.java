package com.zkrypto.zkMatch.domain.corporation.application.service;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.*;
import com.zkrypto.zkMatch.domain.corporation.application.dto.response.CorporationResponse;
import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.corporation.domain.repository.CorporationRepository;
import com.zkrypto.zkMatch.domain.interview.domain.entity.Interview;
import com.zkrypto.zkMatch.domain.interview.domain.repository.InterviewRepository;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.UpdateApplierStatusCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.CorporationPostResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostApplierResponse;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.global.file.S3Service;
import com.zkrypto.zkMatch.global.rabbitmq.DirectExchangeService;
import com.zkrypto.zkMatch.global.rabbitmq.dto.SendMessage;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CorporationService {
    private final CorporationRepository corporationRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final RecruitRepository recruitRepository;
    private final DirectExchangeService directExchangeService;
    private final S3Service s3Service;
    private final InterviewRepository interviewRepository;

    /**
     * 기업 생성 메서드
     */
    @Transactional
    public void createCorporation(CorporationCreationCommand corporationCreationCommand, MultipartFile file) throws IOException {
        // 기업 중복 확인
        if(corporationRepository.existsCorporationByCorporationName(corporationCreationCommand.getCorporationName())) {
            throw new CustomException(ErrorCode.CORPORATION_DUPLICATION);
        }

        // 인사담당자 아이디 중복 확인
        if(memberRepository.existsByLoginId(corporationCreationCommand.getLoginId())){
            throw new CustomException(ErrorCode.ID_DUPLICATION);
        }

        // 인사담당자 생성
        String hashedPassword = passwordEncoder.encode(corporationCreationCommand.getPassword());
        Member member = Member.from(corporationCreationCommand, hashedPassword);

        // 사업자 등록증 업로드
        String registerFileUrl = "";
        if(file != null && !file.isEmpty()) {
            registerFileUrl = s3Service.uploadFile(file);
        }

        // 기업 생성
        Corporation corporation = Corporation.from(corporationCreationCommand, registerFileUrl, member);
        corporationRepository.save(corporation);
    }

    /**
     * 기업 조회 메서드
     */
    public CorporationResponse getCorporation(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return new CorporationResponse(member.getCorporation().getCorporationName());
    }

    /**
     * 기업 공고 생성 메서드
     */
    public void createPost(UUID memberId, PostCreationCommand postCreationCommand) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 공고 생성
        Post post = Post.from(postCreationCommand, member.getCorporation());
        postRepository.save(post);
    }

    /**
     * 기업 공고 조회 메서드
     */
    public List<CorporationPostResponse> getCorporationPost(UUID memberId, String keyword) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<Post> posts = postRepository.findPostWithKeyword(member.getCorporation(), keyword);
        return posts.stream().map(this::toCorporationPostResponse).toList();
    }

    private CorporationPostResponse toCorporationPostResponse(Post post) {
        // 지원자 조회
        List<Recruit> appliers = recruitRepository.findByPost(post);
        return CorporationPostResponse.from(post, appliers.size());
    }

    /**
     * 공고 지원자 조회 메서드
     */
    public List<PostApplierResponse> getPostApplier(String postId) {
        // 공고 조회
        Post post = postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 지원자 조회
        List<Recruit> appliers = recruitRepository.findByPostWithMember(post);
        return appliers.stream().map(PostApplierResponse::from).toList();
    }

    /**
     * 지원자 상태 업데이트 메서드
     */
    @Transactional
    public void updateApplierStatus(UpdateApplierStatusCommand command) {
        // 지원자 조회
        Recruit recruit = recruitRepository.findRecruitByIdWithMemberAndPost(command.getRecruitId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_APPLIED_TO_POSTING));

        // PENDING, INTERVIEW 는 잘못된 요청
        if(command.getStatus() == Status.PENDING || command.getStatus() == Status.INTERVIEW) {
            throw new CustomException(ErrorCode.NOT_PERMITTED_STATUS);
        }

        // 이미 합격한 지원자인지 확인
        if(recruit.getStatus() == Status.PASS) {
            throw new CustomException(ErrorCode.ALREADY_PASSED);
        }

        // 이미 탈락한 지원자인지 확인
        if(recruit.getStatus() == Status.FAILED) {
            throw new CustomException(ErrorCode.ALREADY_FAILED);
        }

        // 상태 업데이트
        recruit.updateStatus(command.getStatus());

        // 합격 이메일 전송
        directExchangeService.send(SendMessage.from(recruit, command.getStatus()));
    }

    /**
     * 공고 업데이트 메서드
     */
    @Transactional
    public void updatePost(PostUpdateCommand postUpdateCommand) {
        // 공고 조회
        Post post = postRepository.findById(UUID.fromString(postUpdateCommand.getPostId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 공고 업데이트
        post.update(postUpdateCommand);
    }

    /**
     * 면접 일정 생성 메서드
     */
    @Transactional
    public void createInterview(InterviewCreationCommand interviewCreationCommand) {
        // 지원 이력 조회
        Recruit recruit = recruitRepository.findById(interviewCreationCommand.getRecruitId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECRUIT));

        // 면접 이력 조회
        if(recruit.getStatus() == Status.INTERVIEW) {
            throw new CustomException(ErrorCode.ALREADY_HAS_INTERVIEW);
        }

        // 면접 생성
        Interview interview = Interview.from(interviewCreationCommand);
        interviewRepository.save(interview);
        recruit.setInterview(interview);
    }

    /**
     * 면접 일정 수정 메서드
     */
    @Transactional
    public void updateInterview(InterviewUpdateCommand interviewUpdateCommand) {
        // 면접 일정 조회
        Interview interview = interviewRepository.findByRecruitId(interviewUpdateCommand.getRecruitId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_INTERVIEW));

        // 수정
        interview.update(interviewUpdateCommand);
    }

    /**
     * 지원자 평가 메서드
     */
    @Transactional
    public void evaluateApplier(EvaluationCreationCommand evaluationCreationCommand) {
        // 지원 이력 조회
        Recruit recruit = recruitRepository.findById(evaluationCreationCommand.getRecruitId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECRUIT));

        // 평가 대상자인지 확인
        if(recruit.getStatus() != Status.PASS) {
            throw new CustomException(ErrorCode.NOT_EVALUATE_TARGET);
        }

        // 평가 생성
        recruit.setEvaluation(evaluationCreationCommand.getEvaluation());
    }
}
