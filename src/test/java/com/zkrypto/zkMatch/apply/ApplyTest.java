package com.zkrypto.zkMatch.apply;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.member.application.dto.request.ResumeCreationCommand;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResumeResponse;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeRepository;
import com.zkrypto.zkMatch.global.crypto.SaltUtil;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
public class ApplyTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PostService postService;

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    CorporationService corporationService;
    @Autowired
    private PostRepository postRepository;

    @Test
    void 지원_테스트() {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "test");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "test");
        ReflectionUtil.setter(corporationCreationCommand, "password", "test");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 관리자 조회
        Member admin = memberRepository.findMemberByLoginId("test").get();

        // 공고 생성
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "title", "test");
        ReflectionUtil.setter(postCreationCommand, "majorRequirement", "정보시스템학과");
        ReflectionUtil.setter(postCreationCommand, "educationRequirement", "4년제");
        ReflectionUtil.setter(postCreationCommand, "experienceRequirement", 1);
        ReflectionUtil.setter(postCreationCommand, "licenseRequirement", List.of("정보처리기사", "빅데이터분석기사"));
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 공고 조회
        Post post = postRepository.findPostByTitle("test").get();

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "salt", SaltUtil.generateSalt());
        memberRepository.save(member);

        // 이력서 생성
        String edu = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"univ\": \"한양대학교\",\n" +
                "    \"univType\": \"4년제\",\n" +
                "    \"maj\": \"정보시스템학과\",\n" +
                "    \"degree\": \"학사\",\n" +
                "    \"registerNumber\": \"hyu-20251234125\"\n" +
                "}";
        ResumeCreationCommand command = new ResumeCreationCommand();
        ReflectionUtil.setter(command, "resumeType", ResumeType.EDUCATION);
        ReflectionUtil.setter(command, "data", edu);
        memberService.createMemberResume(member.getMemberId(), command);

        String license = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"pid\": \"010101-0000000\",\n" +
                "    \"license\": \"정보처리기사\",\n" +
                "    \"expired\": \"20250707\"\n" +
                "}";
        ResumeCreationCommand command1 = new ResumeCreationCommand();
        ReflectionUtil.setter(command1, "resumeType", ResumeType.LICENSE);
        ReflectionUtil.setter(command1, "data", license);
        memberService.createMemberResume(member.getMemberId(), command1);

        String experience = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"startdate\": \"2025.03.01\",\n" +
                "    \"expdate\": \"2026.03.02\",\n" +
                "    \"department\": \"연구생\",\n" +
                "    \"position\": \"연구생\",\n" +
                "    \"company\": \"지크립토\"\n" +
                "}";
        ResumeCreationCommand command2 = new ResumeCreationCommand();
        ReflectionUtil.setter(command2, "resumeType", ResumeType.EXPERIENCE);
        ReflectionUtil.setter(command2, "data", experience);
        memberService.createMemberResume(member.getMemberId(), command2);

        // 멤버 이력 조회
        List<Resume> encResumes = resumeRepository.getResumesByMember(member);

        // 지원 조건 확인
        Boolean check = postService.checkApplyCondition(encResumes, member, post);

        // 검증
        Assertions.assertThat(check).isTrue();
    }
}
