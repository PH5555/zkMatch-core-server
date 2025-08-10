package com.zkrypto.zkMatch.domain.auth.application.service;

import com.zkrypto.zkMatch.domain.auth.application.dto.request.EmailVerificationCommand;
import com.zkrypto.zkMatch.domain.auth.application.dto.request.ReissueCommand;
import com.zkrypto.zkMatch.domain.auth.application.dto.request.SignInCommand;
import com.zkrypto.zkMatch.domain.auth.application.dto.request.SignUpCommand;
import com.zkrypto.zkMatch.domain.auth.application.dto.response.AuthTokenResponse;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.global.jwt.JwtTokenHandler;
import com.zkrypto.zkMatch.global.rabbitmq.DirectExchangeService;
import com.zkrypto.zkMatch.global.rabbitmq.dto.SendMessage;
import com.zkrypto.zkMatch.global.redis.RedisService;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenHandler jwtTokenHandler;
    private final RedisService redisService;
    private final DirectExchangeService directExchangeService;

    /**
     *  회원 가입 메서드
     */
    @Transactional
    public void signUp(SignUpCommand signUpCommand) {
        // 아이디 중복 확인
        if(memberRepository.existsByLoginId(signUpCommand.getLoginId())) {
           throw new CustomException(ErrorCode.ID_DUPLICATION);
        }

        // 이메일 인증번호 확인
        String authKey = redisService.getData(signUpCommand.getCi()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AUTH_NUMBER));
        if(!authKey.equals(signUpCommand.getEmailAuthNumber())) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_AUTH);
        }

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());

        // redis에서 키 삭제
        redisService.deleteData(signUpCommand.getCi());

        // 유저 생성
        Member member = Member.from(signUpCommand, hashedPassword);
        memberRepository.save(member);
    }


    /**
     *  로그인 메서드
     */
    @Transactional
    public AuthTokenResponse signIn(SignInCommand signInCommand) {
        // 아이디 확인
        Member member = memberRepository.findMemberByLoginId(signInCommand.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_ID));

        // 비밀번호 확인
        if(!passwordEncoder.matches(signInCommand.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_MEMBER_PASSWORD);
        }

        // 토큰 발급
        AuthTokenResponse tokenResponse = AuthTokenResponse.from(jwtTokenHandler.generate(member));

        // 리프레쉬 토큰 저장
        member.storeRefreshToken(tokenResponse.getRefreshToken());
        memberRepository.save(member);

        // 토큰 리턴
        return tokenResponse;
    }

    /**
     *  reissue 메서드
     */
    @Transactional
    public AuthTokenResponse reissue(ReissueCommand reissueCommand) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(reissueCommand.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 토큰 일치 확인
        validate(reissueCommand.getRefreshToken(), member);

        // 토큰 재발급
        AuthTokenResponse tokenResponse = AuthTokenResponse.from(jwtTokenHandler.generate(member));

        // 토큰 저장
        member.storeRefreshToken(tokenResponse.getRefreshToken());
        return tokenResponse;
    }

    private void validate(String refreshToken, Member member) {
        // 유효한 리프레쉬인지 확인
        jwtTokenHandler.validateRefreshToken(refreshToken);

        // 유저와 일치하는 리프레쉬토큰인지 확인
        if (!member.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("리프레시 토큰이 일치하지 않습니다.");
        }
    }

    /**
     * 이메일 인증 요청 메서드
     */
    public void verifyEmail(EmailVerificationCommand emailVerificationCommand) {
        // 랜덤 키 생성
        String randomKey = UUID.randomUUID().toString().substring(0, 8);

        // 키 redis에 저장 10분 동안 유효
        redisService.setData(emailVerificationCommand.getCi(), randomKey, 600000L);

        // 이메일 전송
        directExchangeService.send(SendMessage.from(emailVerificationCommand.getEmail(), randomKey));
    }
}
