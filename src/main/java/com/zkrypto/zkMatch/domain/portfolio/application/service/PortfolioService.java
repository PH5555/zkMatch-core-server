package com.zkrypto.zkMatch.domain.portfolio.application.service;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.portfolio.domain.repository.PortfolioRepository;
import com.zkrypto.zkMatch.global.file.S3Service;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    /**
     * 자기소개서 업로드 메서드
     */
//    @Transactional
//    public void uploadPersonalStatement(UUID memberId, MultipartFile file) throws IOException {
//        // 멤버 조회
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
//
//        // 파일 업로드
//        String url = s3Service.uploadFile(file);
//
//        // 자기소개서 등록
//        member.getPortfolio().setPersonalStatement(url);
//    }
}
