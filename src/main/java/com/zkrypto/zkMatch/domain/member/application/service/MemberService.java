package com.zkrypto.zkMatch.domain.member.application.service;

import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberPostResponse;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResponse;
import com.zkrypto.zkMatch.domain.scrab.application.dto.response.ScrabResponse;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.domain.scrab.domain.entity.Scrab;
import com.zkrypto.zkMatch.domain.scrab.domain.repository.ScrabRepository;
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
    public void uploadPortfolio(UUID memberId, MultipartFile file) throws IOException {
        // 멤버 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 파일 업로드
        String url = s3Service.uploadFile(file);

        // 자기소개서 등록
        member.setPortfolioUrl(url);
    }
}
