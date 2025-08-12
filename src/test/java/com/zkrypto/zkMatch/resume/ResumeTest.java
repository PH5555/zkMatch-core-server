package com.zkrypto.zkMatch.resume;

import com.zkrypto.zkMatch.domain.member.application.dto.request.ResumeCreationCommand;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResumeResponse;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.resume.domain.constant.BaseVc;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import com.zkrypto.zkMatch.global.crypto.SaltUtil;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Rollback
@SpringBootTest
@Slf4j
public class ResumeTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void VC_변환_테스트() {
        String data = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"pid\": \"020306-0000000\",\n" +
                "    \"license\": \"정보처리기사\",\n" +
                "    \"expired\": \"2030-05-30\"\n" +
                "}";
        boolean res = BaseVc.checkVcFormat(data, ResumeType.LICENSE);
        Assertions.assertThat(res).isTrue();
    }

    @Test
    void 이력서_복호화_테스트() {
        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "salt", SaltUtil.generateSalt());
        memberRepository.save(member);

        // 이력서 생성
        String data = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"univ\": \"한양대학교\",\n" +
                "    \"univType\": 4,\n" +
                "    \"maj\": \"정보시스템\",\n" +
                "    \"degree\": \"학사\",\n" +
                "    \"registerNumber\": \"hyu-20251234125\"\n" +
                "}";
        ResumeCreationCommand command = new ResumeCreationCommand();
        ReflectionUtil.setter(command, "resumeType", ResumeType.EDUCATION);
        ReflectionUtil.setter(command, "data", data);
        memberService.createMemberResume(member.getMemberId(), command);

        // 이력서 조회
        List<MemberResumeResponse> memberResume = memberService.getMemberResume(member.getMemberId());

        // 검증
        Assertions.assertThat(memberResume.get(0).getResumeType()).isEqualTo(ResumeType.EDUCATION);
    }

    @Test
    void 이력서_삭제_테스트() {
        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "salt", SaltUtil.generateSalt());
        memberRepository.save(member);

        // 이력서 생성
        String data = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"univ\": \"한양대학교\",\n" +
                "    \"univType\": 4,\n" +
                "    \"maj\": \"정보시스템\",\n" +
                "    \"degree\": \"학사\",\n" +
                "    \"registerNumber\": \"hyu-20251234125\"\n" +
                "}";
        ResumeCreationCommand command = new ResumeCreationCommand();
        ReflectionUtil.setter(command, "resumeType", ResumeType.EDUCATION);
        ReflectionUtil.setter(command, "data", data);
        memberService.createMemberResume(member.getMemberId(), command);

        // 이력서 조회
        List<MemberResumeResponse> memberResume = memberService.getMemberResume(member.getMemberId());

        // 이력서 삭제
        memberService.deleteMemberResume(member.getMemberId(), memberResume.get(0).getResumeId().toString());

        // 이력서 조회
        List<MemberResumeResponse> res = memberService.getMemberResume(member.getMemberId());

        // 검증
        Assertions.assertThat(res.size()).isEqualTo(0);
    }
}
