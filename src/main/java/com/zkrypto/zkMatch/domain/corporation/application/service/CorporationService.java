package com.zkrypto.zkMatch.domain.corporation.application.service;

import com.zkrypto.zkMatch.api.cas.CasFeign;
import com.zkrypto.zkMatch.api.cas.dto.request.SaveUserInfoResDto;
import com.zkrypto.zkMatch.api.issuer.IssuerFeign;
import com.zkrypto.zkMatch.api.tas.TasFeign;
import com.zkrypto.zkMatch.api.tas.constant.VcPlan;
import com.zkrypto.zkMatch.api.tas.dto.response.RequestVcPlanListResDto;
import com.zkrypto.zkMatch.domain.corporation.application.dto.request.*;
import com.zkrypto.zkMatch.domain.corporation.application.dto.response.*;
import com.zkrypto.zkMatch.domain.corporation.domain.constant.SaveUserInfoReqDto;
import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.corporation.domain.repository.CorporationRepository;
import com.zkrypto.zkMatch.domain.evaluation.domain.entity.Evaluation;
import com.zkrypto.zkMatch.domain.evaluation.domain.repository.EvaluationRepository;
import com.zkrypto.zkMatch.domain.interview.domain.entity.Interview;
import com.zkrypto.zkMatch.domain.interview.domain.repository.InterviewRepository;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResumeResponse;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.offer.domain.entity.Offer;
import com.zkrypto.zkMatch.domain.offer.domain.repository.OfferRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.UpdateApplierStatusCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.CorporationPostResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostApplierResponse;
import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.domain.resume.domain.constant.*;
import com.zkrypto.zkMatch.domain.resume.domain.entity.AppliedResume;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import com.zkrypto.zkMatch.domain.resume.domain.repository.AppliedResumeRepository;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeCustomRepository;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeCustomRepositoryImpl;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeRepository;
import com.zkrypto.zkMatch.global.crypto.AesUtil;
import com.zkrypto.zkMatch.global.file.S3Service;
import com.zkrypto.zkMatch.global.rabbitmq.DirectExchangeService;
import com.zkrypto.zkMatch.global.rabbitmq.dto.EmailMessage;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import com.zkrypto.zkMatch.global.utils.ListUtil;
import com.zkrypto.zkMatch.global.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Period;
import java.util.*;

@Slf4j
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
    private final OfferRepository offerRepository;
    private final EvaluationRepository evaluationRepository;
    private final AppliedResumeRepository appliedResumeRepository;
    private final ResumeCustomRepository resumeCustomRepository;
    private final TasFeign tasFeign;
    private final CasFeign casFeign;
    private final IssuerFeign issuerFeign;
    private final ResumeRepository resumeRepository;

    /**
     * 기업 생성 메서드
     */
    @Transactional
    public void createCorporation(CorporationCreationCommand corporationCreationCommand, MultipartFile file) {
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
    public void updateApplierStatus(String recruitId, UpdateApplierStatusCommand command) {
        // 지원자 조회
        Recruit recruit = recruitRepository.findRecruitByIdWithMemberAndPost(Long.parseLong(recruitId))
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
        directExchangeService.send(EmailMessage.from(recruit, command.getStatus()));
    }

    /**
     * 공고 업데이트 메서드
     */
    @Transactional
    public void updatePost(String postId, PostUpdateCommand postUpdateCommand) {
        // 공고 조회
        Post post = postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 공고 업데이트
        post.update(postUpdateCommand);
    }

    /**
     * 면접 일정 생성 메서드
     */
    @Transactional
    public void createInterview(String recruitId, InterviewCreationCommand interviewCreationCommand) {
        // 지원 이력 조회
        Recruit recruit = recruitRepository.findById(Long.parseLong(recruitId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECRUIT));

        // 면접 이력 조회
        if(recruit.getStatus() != Status.PENDING) {
            throw new CustomException(ErrorCode.NOT_INTERVIEW_TARGET);
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
    public void updateInterview(String recruitId, InterviewUpdateCommand interviewUpdateCommand) {
        // 면접 일정 조회
        Interview interview = interviewRepository.findByRecruitId(Long.parseLong(recruitId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_INTERVIEW));

        // 수정
        interview.update(interviewUpdateCommand);
    }

    /**
     * 지원자 평가 메서드
     */
    @Transactional
    public void evaluateApplier(String recruitId, EvaluationCreationCommand evaluationCreationCommand) {
        // 지원 이력 조회
        Recruit recruit = recruitRepository.findById(Long.parseLong(recruitId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECRUIT));

        // 평가 대상자인지 확인
        if(recruit.getStatus() != Status.PASS) {
            throw new CustomException(ErrorCode.NOT_EVALUATE_TARGET);
        }

        // 평가 생성
        evaluationRepository.save(Evaluation.from(evaluationCreationCommand, recruit));
    }

    /**
     *  인재 검색 메서드
     */
    public List<CandidateResponse> searchCandidate(List<String> licenses, Integer employPeriod, String educationType) {
        // 필터링 조건에 따른 이력서 조회
        List<Resume> resumes = resumeCustomRepository.findCandidateResume(licenses, employPeriod, educationType);

        // 이력서 멤버마다 구분
        Map<Member, List<Resume>> memberResumeMap = new HashMap<>();
        resumes.forEach(resume -> memberResumeMap.computeIfAbsent(resume.getMember(), k -> new ArrayList<>()).add(resume));

        // 필터링 조건 확인
        List<Member> filteredMember = memberResumeMap.keySet().stream()
                .filter(member -> checkFilterCondition(memberResumeMap.get(member), member, licenses, employPeriod, educationType)).toList();
        return filteredMember.stream().map(this::toCandidateResponse).toList();
    }

    private CandidateResponse toCandidateResponse(Member member) {
        List<Evaluation> evaluations = evaluationRepository.findEvaluationsByMember(member);
        long projectCount = resumeRepository.countByMemberAndResumeType(member, ResumeType.PORTFOLIO);
        double averageRating = evaluations.stream().mapToInt(Evaluation::getRating).average().orElse(0);
        return CandidateResponse.from(member, averageRating, projectCount);
    }

    /**
     * 필터링 조건 확인 메서드
     */
    private Boolean checkFilterCondition(List<Resume> encResumes, Member member, List<String> licenses, int employPeriod, String educationType) {
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
        if(educationType.equals("4년제") && educationVcList.stream().noneMatch(vc -> vc.getUnivType().equals("4년제"))) {
            return false;
        }else if(educationType.equals("2년제") && educationVcList.stream().noneMatch(vc -> vc.getUnivType().equals("4년제") || vc.getUnivType().equals("2년제"))) {
            return false;
        }

        // 자격증 확인
        if(!ListUtil.isEmpty(licenses) && licenseVcList.stream().noneMatch(vc -> licenses.contains(vc.getLicense()))) {
            return false;
        }

        // 경력 확인
        if(employPeriod > 0 && experienceVcList.stream().noneMatch(vc -> employPeriod <= Period.between(DateFormatter.format(vc.getStartdate()), DateFormatter.format(vc.getExpdate())).getYears())) {
            return false;
        }

        return true;
    }

    /**
     * 채용 제안 메서드
     */
    public void offerCandidate(UUID memberId, CandidateOfferCommand candidateOfferCommand) {
        // 멤버 조회
        Member member = memberRepository.findById(UUID.fromString(candidateOfferCommand.getMemberId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 회사 조회
        Corporation corporation = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)).getCorporation();

        // 제안 생성
        offerRepository.save(new Offer(member, corporation));
    }

    /**
     * 지원자 평가 조회 메서드
     */
    public List<EvaluationResponse> getEvaluation(String recruitId) {
        // 지원 이력 조회
        Recruit recruit = recruitRepository.findById(Long.parseLong(recruitId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECRUIT));

        // 평가 조회
        return evaluationRepository.findEvaluationsByMember(recruit.getMember())
                .stream().map(EvaluationResponse::from).toList();
    }

    /**
     * 면접 조회 메서드
     */
    public InterviewResponse getInterview(String recruitId) {
        // 지원 이력 조회
        Recruit recruit = recruitRepository.findById(Long.parseLong(recruitId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECRUIT));

        // 면접 조회
        if(recruit.getInterview() == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_INTERVIEW);
        }

        return InterviewResponse.from(recruit.getInterview());
    }

    /**
     * 지원자 상세 정보 조회 메서드
     */
    public ApplierDetailResponse getApplierDetail(String recruitId) {
        // 지원 이력 조회
        Recruit recruit = recruitRepository.findById(Long.parseLong(recruitId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECRUIT));

        // 유저가 공개한 이력서 조회
        List<AppliedResume> encResumes = appliedResumeRepository.findAppliedResumesByRecruit(recruit);

        // 이력서 복호화
        List<MemberResumeResponse> resumes = encResumes.stream().map(encResume -> {
            String plainText = AesUtil.decrypt(encResume.getResume().getEncData(), recruit.getMember().getSalt());
            Object vc = BaseVc.mappingVc(plainText, encResume.getResume().getResumeType());
            return new MemberResumeResponse(encResume.getResume().getResumeId(), encResume.getResume().getResumeType(), vc, encResume.getResume().getDid());
        }).toList();

        return ApplierDetailResponse.from(recruit, resumes);
    }
}
