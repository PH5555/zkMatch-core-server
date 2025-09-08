package com.zkrypto.zkMatch.domain.post.application.service;

import com.zkrypto.zkMatch.domain.application.domain.constant.Status;
import com.zkrypto.zkMatch.domain.application.domain.entity.Application;
import com.zkrypto.zkMatch.domain.application.domain.repository.ApplicationRepository;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResumeResponse;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.CompleteApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.ApplyQrResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostCustomRepository;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.domain.resume.domain.constant.*;
import com.zkrypto.zkMatch.domain.resume.domain.entity.AppliedResume;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import com.zkrypto.zkMatch.domain.resume.domain.repository.AppliedResumeRepository;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeRepository;
import com.zkrypto.zkMatch.global.crypto.AesUtil;
import com.zkrypto.zkMatch.global.qr.QrMaker;
import com.zkrypto.zkMatch.global.redis.RedisService;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import com.zkrypto.zkMatch.global.utils.ListUtil;
import com.zkrypto.zkMatch.global.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RecruitRepository recruitRepository;
    private final ResumeRepository resumeRepository;
    private final AppliedResumeRepository appliedResumeRepository;
    private final ApplicationRepository applicationRepository;

    /**
     * 공고 조회 메서드
     */
    public List<PostResponse> getPost(PostType postType) {
        List<Post> posts = postRepository.findPostWithType(postType);
        return posts.stream().map(PostResponse::from).toList();
    }

    /**
     * 단순 공고 지원 메서드
     */
    public void applyPost(UUID memberId, String postId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 공고 확인
        Post post = postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 이미 지원한 공고인지 확인
        if(recruitRepository.existsByMemberAndPost(member, post)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED_POST);
        }

        // 제출 기한이 지난 공고인지 확인
        if(LocalDateTime.now().isAfter(post.getEndDate())) {
            throw new CustomException(ErrorCode.EXPIRED_POST);
        }

        // 지원
        Recruit recruit = new Recruit(post, member);
        recruitRepository.save(recruit);
    }

    /**
     * 지원 조건 확인 메서드
     */
    public Boolean checkApplyCondition(List<Resume> encResumes, Member member, Post post) {
        List<EducationVc> educationVcList = new ArrayList<>();
        List<LicenseVc> licenseVcList = new ArrayList<>();
        List<ExperienceVc> experienceVcList = new ArrayList<>();

        encResumes.forEach(encResume -> {
            String plainText = AesUtil.decrypt(encResume.getEncData(), member.getSalt());
            Object vc = BaseVc.mappingVc(plainText, encResume.getResumeType());
            if(encResume.getResumeType() == ResumeType.EDUCATION) {
                educationVcList.add((EducationVc) vc);
            }
            else if(encResume.getResumeType() == ResumeType.EXPERIENCE) {
                experienceVcList.add((ExperienceVc) vc);
            }
            else if(encResume.getResumeType() == ResumeType.LICENSE) {
                licenseVcList.add((LicenseVc) vc);
            }
        });

        // 학력 확인
        if(post.getEducationRequirement().equals("4년제") && educationVcList.stream().noneMatch(vc -> vc.getUnivType().equals("4년제"))) {
            return false;
        }else if(post.getEducationRequirement().equals("2년제") && educationVcList.stream().noneMatch(vc -> vc.getUnivType().equals("4년제") || vc.getUnivType().equals("2년제"))) {
            return false;
        }

        // 자격증 확인
        if(!ListUtil.isEmpty(post.getLicenseRequirement()) && licenseVcList.stream().noneMatch(vc -> post.getLicenseRequirement().contains(vc.getLicense()))) {
            return false;
        }

        // 학과 확인
        if(!StringUtil.isEmpty(post.getMajorRequirement()) && educationVcList.stream().noneMatch(vc -> vc.getMaj().equals(post.getMajorRequirement()))) {
            return false;
        }

        // 경력 확인
        if(post.getExperienceRequirement() > 0 && experienceVcList.stream().noneMatch(vc -> post.getExperienceRequirement() <= Period.between(DateFormatter.format(vc.getStartdate()), DateFormatter.format(vc.getExpdate())).getYears())) {
            return false;
        }

        return true;
    }

    /**
     * 관심 공고 조회 메서드
     */
    public List<PostResponse> getInterestPost(UUID memberId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 관심 분야 조회
        return member.getInterests().stream().flatMap(interest -> postRepository.findByCategory(interest).stream())
                .map(PostResponse::from).toList();
    }

    /**
     * 지원 QR 생성 메서드
     */
    @Transactional
    public ApplyQrResponse createApplyQr(UUID memberId, String postId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 공고 확인
        Post post = postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 이미 지원한 공고인지 확인
        if(recruitRepository.existsByMemberAndPost(member, post)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED_POST);
        }

        // 제출 기한이 지난 공고인지 확인
        if(LocalDateTime.now().isAfter(post.getEndDate())) {
            throw new CustomException(ErrorCode.EXPIRED_POST);
        }

        // 공고 지원 요청 저장
        applicationRepository.findApplicationByMemberAndPost(member, post).ifPresent(applicationRepository::delete);
        Application application = applicationRepository.save(new Application(member, post));

        // QR 생성
        ApplyQrResponse resultDto = ApplyQrResponse.from(application);
        byte[] qrImage = QrMaker.makeQr(resultDto);

        // 반환 값에 저장
        resultDto.setQrImage(qrImage);

        return resultDto;
    }


    /**
     *  지원 완료 메서드
     */
    @Transactional
    public void completeApply(UUID memberId, String postId, CompleteApplyCommand completeApplyCommand) {
        // 멤버 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 공고 확인
        Post post = postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 지원 정보 조회
        Application applyData = applicationRepository.findApplicationByMemberAndPost(member, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPLY_SESSION));

        // 지원 성공인지 확인
        if(applyData.getStatus() != Status.SUCCESS) {
            throw new CustomException(ErrorCode.INVALID_APPLY_CONDITION);
        }

        // 같은 공고에 대한 요청인지 확인
        if(applyData.getPost().getPostId() != post.getPostId()) {
            throw new CustomException(ErrorCode.INVALID_APPLY_CONDITION);
        }

        // 유효시간 확인
        if(LocalDateTime.now().isAfter(applyData.getValidTime())) {
            throw new CustomException(ErrorCode.EXPIRED_APPLICATION);
        }

        // 이력서 조회
        List<Resume> resumes = completeApplyCommand.getOpenedResumes().stream()
                .map(resumeId -> resumeRepository.findById(resumeId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESUME)))
                .toList();

        // 지원 이력 생성
        Recruit recruit = new Recruit(post, member);
        recruitRepository.save(recruit);

        // 공개 이력서 설정
        resumes.forEach(resume -> {
            AppliedResume appliedResume = new AppliedResume(recruit, resume);
            appliedResumeRepository.save(appliedResume);
        });

        // 지원 정보 삭제
        applicationRepository.delete(applyData);
    }
}
