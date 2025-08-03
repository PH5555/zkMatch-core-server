package com.zkrypto.zkMatch.domain.corporation.application.service;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.dto.response.CorporationResponse;
import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.corporation.domain.repository.CorporationRepository;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PassApplierCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.CorporationPostResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostApplierResponse;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.global.rabbitmq.DirectExchangeService;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 기업 생성 메서드
     */
    @Transactional
    public void createCorporation(CorporationCreationCommand corporationCreationCommand) {
        // 기업 중복 확인
        if(corporationRepository.existsCorporationByRegisterNumber(corporationCreationCommand.getCorporationRegisterNumber())) {
            throw new CustomException(ErrorCode.REGISTER_NUMBER_DUPLICATION);
        }

        // 인사담당자 아이디 중복 확인
        if(memberRepository.existsByLoginId(corporationCreationCommand.getLoginId())){
            throw new CustomException(ErrorCode.ID_DUPLICATION);
        }

        // 인사담당자 생성
        String hashedPassword = passwordEncoder.encode(corporationCreationCommand.getPassword());
        Member member = Member.from(corporationCreationCommand.getLoginId(), hashedPassword);

        // 기업 생성
        Corporation corporation = Corporation.from(corporationCreationCommand, member);
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
    public List<CorporationPostResponse> getCorporationPost(UUID memberId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<Post> posts = postRepository.findPostByCorporation(member.getCorporation());
        return posts.stream().map(this::toCorporationPostResponse).toList();
    }

    private CorporationPostResponse toCorporationPostResponse(Post post) {
        // 지원자 조회
        List<Recruit> appliers = recruitRepository.findByPost(post);

        // 합격자 필터링
        List<Recruit> passer = appliers.stream().filter(applier -> applier.getStatus() == Status.PASS).toList();
        return CorporationPostResponse.from(post, appliers.size(), passer.size());
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
     * 지원자 합격 메서드
     */
    @Transactional
    public void passApplier(String postId, PassApplierCommand passApplierCommand) {
        // 공고 조회
        Post post = postRepository.findByIdWithCorporation(UUID.fromString(postId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 지원자 조회
        Recruit recruit = recruitRepository.findRecruitByMemberAndPost(UUID.fromString(passApplierCommand.getApplierId()), post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_APPLIED_TO_POSTING));

        // 이미 합격한 지원자인지 확인
        if(recruit.getStatus() == Status.PASS) {
            throw new CustomException(ErrorCode.ALREADY_PASSED);
        }

        // 합격 처리
        recruit.pass();

        // 합격 이메일 전송
        directExchangeService.send(post.getCorporation(), recruit.getMember());
    }
}
