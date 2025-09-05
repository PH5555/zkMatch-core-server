package com.zkrypto.zkMatch.mockUp;

import com.zkrypto.zkMatch.domain.auth.application.dto.request.SignUpCommand;
import com.zkrypto.zkMatch.domain.auth.application.service.AuthService;
import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MockUpHelper implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final CorporationService corporationService;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;
    private final PostRepository postRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createMemberAccount();
        createCorporationAccount();
        createPost();
//        apply();
    }

    private void createMemberAccount() {
        SignUpCommand signUpCommand = new SignUpCommand();
        ReflectionUtil.setter(signUpCommand, "ci", "testCi");
        ReflectionUtil.setter(signUpCommand, "loginId", "1234");
        ReflectionUtil.setter(signUpCommand, "password", "1234");
        ReflectionUtil.setter(signUpCommand, "email", "lmkn5342@naver.com");
        ReflectionUtil.setter(signUpCommand, "emailAuthNumber", "test");
        ReflectionUtil.setter(signUpCommand, "name", "김동현");
        ReflectionUtil.setter(signUpCommand, "phoneNumber", "01041461092");
        ReflectionUtil.setter(signUpCommand, "birth", "020306");
        ReflectionUtil.setter(signUpCommand, "interests", List.of("백엔드", "블록체인"));
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());

        Member member = Member.from(signUpCommand, hashedPassword);
        memberRepository.save(member);
    }

    private void createCorporationAccount() {
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationType", "중소기업");
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "corporationAddress", "한양대학교 HIT 302호");
        ReflectionUtil.setter(corporationCreationCommand, "corporationUrl", "https://www.zkrypto.com/");
        ReflectionUtil.setter(corporationCreationCommand, "industryType", "블록체인");
        ReflectionUtil.setter(corporationCreationCommand, "name", "김두연");
        ReflectionUtil.setter(corporationCreationCommand, "email", "kdy@zkrypto.com");
        ReflectionUtil.setter(corporationCreationCommand, "phoneNumber", "01000000000");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "admin");
        ReflectionUtil.setter(corporationCreationCommand, "password", "admin");
        corporationService.createCorporation(corporationCreationCommand, null);
    }

    private void createPost() {
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "title", "지크립토 백엔드 개발자 채용");
        ReflectionUtil.setter(postCreationCommand, "content", "블록체인 잘알고 nodejs 할줄 아는 개발자 구합니다~");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(LocalDate.of(2025, 8, 14), LocalTime.of(0, 0)));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(LocalDate.of(2025, 10, 14), LocalTime.of(0, 0)));
        ReflectionUtil.setter(postCreationCommand, "majorRequirement", "정보시스템학과");
        ReflectionUtil.setter(postCreationCommand, "educationRequirement", "4년제");
        ReflectionUtil.setter(postCreationCommand, "experienceRequirement", 2);
        ReflectionUtil.setter(postCreationCommand, "licenseRequirement", List.of("정보처리기사", "빅데이터분석기사"));
        ReflectionUtil.setter(postCreationCommand, "salaryStart", 5000);
        ReflectionUtil.setter(postCreationCommand, "salaryEnd", 7000);
        ReflectionUtil.setter(postCreationCommand, "workSpace", "서울특별시 성동구");
        ReflectionUtil.setter(postCreationCommand, "workType", "정규직");
        ReflectionUtil.setter(postCreationCommand, "category", List.of("백엔드", "블록체인"));
        ReflectionUtil.setter(postCreationCommand, "postType", PostType.EMPLOYMENT);


        Member admin = memberRepository.findMemberByLoginId("admin").get();
        corporationService.createPost(admin.getMemberId(), postCreationCommand);
    }

    private void apply() {
        Member member = memberRepository.findMemberByLoginId("1234").get();
        Post post = postRepository.findPostByTitle("지크립토 백엔드 개발자 채용").get();
        postService.applyPost(member.getMemberId(), post.getPostId().toString());
    }
}
