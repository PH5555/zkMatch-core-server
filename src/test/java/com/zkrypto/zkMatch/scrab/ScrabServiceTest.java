package com.zkrypto.zkMatch.scrab;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.domain.scrab.application.dto.request.ScrabCommand;
import com.zkrypto.zkMatch.domain.scrab.application.dto.response.ScrabResponse;
import com.zkrypto.zkMatch.domain.scrab.application.service.ScrabService;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
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
@Transactional
@Rollback
public class ScrabServiceTest {
    @Autowired
    CorporationService corporationService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Autowired
    ScrabService scrabService;
    @Autowired
    private MemberService memberService;

    @Test
    void 스크랩_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2026, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "title", "하이");
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 스크랩
        ScrabCommand scrabCommand = new ScrabCommand();
        ReflectionUtil.setter(scrabCommand, "postId", posts.get(0).getPostId());
        scrabService.scrabPost(member.getMemberId(), scrabCommand);

        // 검증
        List<PostResponse> scrab = memberService.getScrab(member.getMemberId());
        assertThat(scrab.size()).isEqualTo(1);
        assertThat(scrab.get(0).getTitle()).isEqualTo("하이");
    }

    @Test
    void 스크랩_해제_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2026, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "title", "하이");
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 스크랩
        ScrabCommand scrabCommand = new ScrabCommand();
        ReflectionUtil.setter(scrabCommand, "postId", posts.get(0).getPostId());
        scrabService.scrabPost(member.getMemberId(), scrabCommand);

        // 스크랩 해제
        scrabService.scrabPost(member.getMemberId(), scrabCommand);

        // 검증
        List<PostResponse> scrab = memberService.getScrab(member.getMemberId());
        assertThat(scrab.size()).isEqualTo(0);
    }
}
