package com.zkrypto.zkMatch.post;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.corporation.domain.repository.CorporationRepository;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@Rollback
@Slf4j
public class PostServiceTest {
    @Autowired
    CorporationService corporationService;

    @Autowired
    RecruitRepository recruitRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Test
    void 공고_조회_테스트() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 관리자 조회
        Member admin = memberRepository.findMemberByLoginId("1234").get();

        // 공고 생성
        corporationService.createPost(admin.getMemberId(), new PostCreationCommand());

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 검증
        Assertions.assertThat(posts.size()).isEqualTo(1);
    }

    @Test
    void 관심_공고_조회_테스트() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 관리자 조회
        Member admin = memberRepository.findMemberByLoginId("1234").get();

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "interests", List.of("백엔드"));
        memberRepository.save(member);

        // 공고 생성
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "title", "프론트 채용");
        ReflectionUtil.setter(postCreationCommand, "category", List.of("개발", "프론트엔드"));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        PostCreationCommand postCreationCommand1 = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand1, "title", "백엔드 채용");
        ReflectionUtil.setter(postCreationCommand1, "category", List.of("개발", "백엔드"));
        corporationService.createPost(admin.getMemberId(), postCreationCommand1);

        // 공고 조회
        List<PostResponse> posts = postService.getInterestPost(member.getMemberId());

        // 검증
        Assertions.assertThat(posts.size()).isEqualTo(1);
        Assertions.assertThat(posts.get(0).getTitle()).isEqualTo("백엔드 채용");
    }

    @Test
    void 공고_지원_테스트() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 관리자 조회
        Member admin = memberRepository.findMemberByLoginId("1234").get();

        // 공고 생성
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 지원
        PostApplyCommand postApplyCommand = new PostApplyCommand();
        ReflectionUtil.setter(postApplyCommand, "postId", posts.get(0).getPostId());
        postService.applyPost(member.getMemberId(), postApplyCommand);

        // 검증
        List<Recruit> recruit = recruitRepository.findByMember(member);
        Assertions.assertThat(recruit.get(0).getPost().getPostId()).isEqualTo(UUID.fromString(posts.get(0).getPostId()));
    }

    @Test
    void 공고_지원_테스트_기한_지남() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 관리자 조회
        Member admin = memberRepository.findMemberByLoginId("1234").get();

        // 공고 생성
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2025, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 지원
        PostApplyCommand postApplyCommand = new PostApplyCommand();
        ReflectionUtil.setter(postApplyCommand, "postId", posts.get(0).getPostId());

        // 공고 제출 기한 지남 검증
        Assertions.assertThatThrownBy(() -> postService.applyPost(member.getMemberId(), postApplyCommand)).hasMessage(ErrorCode.EXPIRED_POST.getMessage());
    }

    @Test
    void 공고_지원_테스트_중복_제출() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 관리자 조회
        Member admin = memberRepository.findMemberByLoginId("1234").get();

        // 공고 생성
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 지원
        PostApplyCommand postApplyCommand = new PostApplyCommand();
        ReflectionUtil.setter(postApplyCommand, "postId", posts.get(0).getPostId());
        postService.applyPost(member.getMemberId(), postApplyCommand);

        // 중복 제출 검증
        Assertions.assertThatThrownBy(() -> postService.applyPost(member.getMemberId(), postApplyCommand)).hasMessage(ErrorCode.ALREADY_APPLIED_POST.getMessage());
    }
}
