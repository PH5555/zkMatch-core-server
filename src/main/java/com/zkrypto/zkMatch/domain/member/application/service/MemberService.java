package com.zkrypto.zkMatch.domain.member.application.service;

import com.zkrypto.zkMatch.domain.member.application.dto.request.ResumeCreationCommand;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberOfferResponse;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberPostResponse;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResponse;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResumeResponse;
import com.zkrypto.zkMatch.domain.offer.domain.entity.Offer;
import com.zkrypto.zkMatch.domain.offer.domain.repository.OfferRepository;
import com.zkrypto.zkMatch.domain.resume.domain.constant.BaseVc;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeRepository;
import com.zkrypto.zkMatch.domain.scrab.application.dto.response.ScrabResponse;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.domain.scrab.domain.entity.Scrab;
import com.zkrypto.zkMatch.domain.scrab.domain.repository.ScrabRepository;
import com.zkrypto.zkMatch.global.crypto.AesUtil;
import com.zkrypto.zkMatch.global.file.S3Service;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RecruitRepository recruitRepository;
    private final S3Service s3Service;
    private final ScrabRepository scrabRepository;
    private final ResumeRepository resumeRepository;
    private final OfferRepository offerRepository;

    /**
     * 멤버 조회 메서드
     */
    public MemberResponse getMember(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return MemberResponse.from(member);
    }

    /**
     * 로그아웃 메서드
     */
    @Transactional
    public void signOut(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        member.signOut();
    }

    /**
     * 지원 내역 조회 메서드
     */
    public List<MemberPostResponse> getPost(UUID memberId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 지원 내역 조회
        List<Recruit> recruit = recruitRepository.findByMemberWithPost(member);

        return recruit.stream().map(MemberPostResponse::from).toList();
    }

    /**
     * 포트폴리오 업로드 메서드
     */
    @Transactional
    public void uploadPortfolio(UUID memberId, MultipartFile file) {
        // 멤버 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 파일 업로드
        String url = s3Service.uploadFile(file);

        // 자기소개서 등록
        member.setPortfolioUrl(url);
    }

    /**
     * 스크랩 공고 조회 메서드
     */
    public List<ScrabResponse> getScrab(UUID memberId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 스크랩 내역 조회
        List<Scrab> scrabs = scrabRepository.findByMember(member);

        return scrabs.stream().map(ScrabResponse::from).toList();
    }

    /**
     * 이력 생성 메서드
     */
    public void createMemberResume(UUID memberId, ResumeCreationCommand resumeCreationCommand) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 데이터 형식 확인
        if(!BaseVc.checkVcFormat(resumeCreationCommand.getData(), resumeCreationCommand.getResumeType())) {
            throw new CustomException(ErrorCode.INVALID_VC_TYPE);
        }

        // 데이터 암호화
        String encData = AesUtil.encrypt(resumeCreationCommand.getData(), member.getSalt());

        // 이력서 저장
        Resume resume = Resume.from(resumeCreationCommand, encData, member);
        resumeRepository.save(resume);
    }


    /**
     * 멤버 전체 이력 조회 메서드
     */
    public List<MemberResumeResponse> getMemberResume(UUID memberId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 이력 조회
        List<Resume> encResumes = resumeRepository.getResumesByMember(member);


        return encResumes.stream().map(encResume -> {
            String plainText = AesUtil.decrypt(encResume.getEncData(), member.getSalt());
            Object vc = BaseVc.mappingVc(plainText, encResume.getResumeType());
            return new MemberResumeResponse(encResume.getResumeId(), encResume.getResumeType(), vc, encResume.getDid());
        }).toList();
    }

    /**
     * 멤버 이력 삭제 메서드
     */
    @Transactional
    public void deleteMemberResume(UUID memberId, String resumeId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 이력 조회
        Resume resume = resumeRepository.getResumeByResumeId(Long.parseLong(resumeId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESUME));

        // 자신의 이력인지 확인
        if(resume.getMember().getMemberId() != member.getMemberId()) {
            throw new CustomException(ErrorCode.NOT_ALLOWED_DELETE_RESUME);
        }

        // 삭제
        resumeRepository.delete(resume);
    }

    /**
     * 채용 제안 조회 메서드
     */
    public List<MemberOfferResponse> getMemberOffer(UUID memberId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 채용 제안 조회
        List<Offer> offers = offerRepository.findOffersByMember(member);
        return offers.stream().map(MemberOfferResponse::from).toList();
    }
}
