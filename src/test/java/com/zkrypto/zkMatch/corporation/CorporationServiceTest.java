package com.zkrypto.zkMatch.corporation;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.dto.response.CorporationResponse;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.corporation.domain.repository.CorporationRepository;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PassApplierCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.CorporationPostResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostApplierResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
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
@Rollback
@Transactional
public class CorporationServiceTest {
    @Autowired
    CorporationService corporationService;

    @Autowired
    CorporationRepository corporationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Test
    void 생성_테스트() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");
        corporationService.createCorporation(corporationCreationCommand, null);

        // member 생성 테스트
        boolean memberTest = memberRepository.existsByLoginId("1234");
        assertThat(memberTest).isTrue();

        // 기업 생성 테스트
        boolean corporationTest = corporationRepository.existsCorporationByCorporationName("지크립토");
        assertThat(corporationTest).isTrue();
    }

    @Test
    void 조회_테스트() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 기업 조회
        Member member = memberRepository.findMemberByLoginId("1234").get();
        CorporationResponse corporation = corporationService.getCorporation(member.getMemberId());

        // 조회 테스트
        assertThat(corporation.getCorporationName()).isEqualTo("지크립토");
    }

    @Test
    void 공고_생성_테스트() throws IOException {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 멤버 조회
        Member member = memberRepository.findMemberByLoginId("1234").get();

        // 공고 생성
        corporationService.createPost(member.getMemberId(), new PostCreationCommand());

        // 생성 테스트
        List<Post> posts = postRepository.findPostByCorporation(member.getCorporation());
        assertThat(posts.size()).isEqualTo(1);
    }

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
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
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

        // 공고 조회
        List<CorporationPostResponse> post = corporationService.getCorporationPost(admin.getMemberId());

        // 검증
        assertThat(post.get(0).getApplierCount()).isEqualTo(1);
        assertThat(post.get(0).getTitle()).isEqualTo("hi");
    }

    @Test
    void 공고_합격_테스트() throws IOException {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
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

        // 지원자 합격
        PassApplierCommand passApplierCommand = new PassApplierCommand();
        ReflectionUtil.setter(passApplierCommand, "applierId", member.getMemberId().toString());
        corporationService.passApplier(posts.get(0).getPostId(), passApplierCommand);

        // 검증
        List<PostApplierResponse> postApplier = corporationService.getPostApplier(posts.get(0).getPostId());
        assertThat(postApplier.get(0).getStatus()).isEqualTo(Status.PASS);
    }
}
