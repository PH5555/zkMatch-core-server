package com.zkrypto.zkMatch.resume;

import com.zkrypto.zkMatch.domain.member.application.dto.request.ResumeCreationCommand;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeCustomRepositoryImpl;
import com.zkrypto.zkMatch.global.crypto.SaltUtil;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Rollback
@Transactional
public class ResumeRepositoryTest {
    @Autowired
    ResumeCustomRepositoryImpl resumeCustomRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    void 필터링_테스트() {
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

        String data1 = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"pid\": \"002000-000000\",\n" +
                "    \"license\": \"정보처리기사\",\n" +
                "    \"expired\": \"20020306\"\n" +
                "}";
        ResumeCreationCommand command1 = new ResumeCreationCommand();
        ReflectionUtil.setter(command1, "resumeType", ResumeType.LICENSE);
        ReflectionUtil.setter(command1, "data", data1);
        memberService.createMemberResume(member.getMemberId(), command1);

        // 이력 조회
        List<Resume> resume = resumeCustomRepository.findCandidateResume(List.of("정보처리기사"), 0, "4년제");

        // 검증
        Assertions.assertThat(resume.size()).isEqualTo(2);
    }
}
