package com.zkrypto.zkMatch.member;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberPostResponse;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.UpdateApplierStatusCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Transactional
@Rollback
public class MemberServiceTest {
    @Autowired
    CorporationService corporationService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Autowired
    MemberService memberService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    private RecruitRepository recruitRepository;

    @Test
    void 나의_지원_내역_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "title", "하이");
        ReflectionUtil.setter(postCreationCommand, "postType", PostType.FREELANCER);

        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        Post post = postRepository.findPostByTitle("하이").get();

        // 공고 지원
        postService.applyPost(member.getMemberId(), post.getPostId().toString());

        // 지원 내역 불러오기
        List<MemberPostResponse> res = memberService.getPost(member.getMemberId(), null);

        // 검증
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.getFirst().getTitle()).isEqualTo("하이");
    }

    @Test
    void 나의_지원_내역_타입_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "title", "하이");
        ReflectionUtil.setter(postCreationCommand, "postType", PostType.FREELANCER);

        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        Post post = postRepository.findPostByTitle("하이").get();

        // 공고 지원
        postService.applyPost(member.getMemberId(), post.getPostId().toString());

        // 지원 내역 불러오기
        List<MemberPostResponse> res = memberService.getPost(member.getMemberId(), PostType.FREELANCER);

        // 검증
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.getFirst().getTitle()).isEqualTo("하이");
    }

    @Test
    void 나의_완료한_프로젝트_조회_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "title", "하이");
        ReflectionUtil.setter(postCreationCommand, "postType", PostType.FREELANCER);

        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        Post post = postRepository.findPostByTitle("하이").get();

        // 공고 지원
        postService.applyPost(member.getMemberId(), post.getPostId().toString());

        // 지원 내역 불러오기
        List<Recruit> recruit = recruitRepository.findRecruitByMember(member);

        // 지원자 합격
        UpdateApplierStatusCommand updateApplierStatusCommand = new UpdateApplierStatusCommand();
        ReflectionUtil.setter(updateApplierStatusCommand, "status", Status.PASS);
        corporationService.updateApplierStatus(recruit.get(0).getId().toString(), updateApplierStatusCommand);

        // 프로젝트 조회
        List<PostResponse> projects = memberService.getMemberFreelancerProjects(member.getMemberId());

        // 검증
        assertThat(projects.size()).isEqualTo(1);
        assertThat(projects.get(0).getTitle()).isEqualTo("하이");
    }
}

