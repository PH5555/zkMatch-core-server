package com.zkrypto.zkMatch.member;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberPostResponse;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
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

    @Test
    void 나의_지원_내역_테스트() throws IOException {
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
        ReflectionUtil.setter(postCreationCommand, "title", "하이");
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

        // 지원 내역 불러오기
        List<MemberPostResponse> post = memberService.getPost(member.getMemberId());

        // 검증
        assertThat(post.size()).isEqualTo(1);
        assertThat(post.getFirst().getTitle()).isEqualTo("하이");
    }
}

