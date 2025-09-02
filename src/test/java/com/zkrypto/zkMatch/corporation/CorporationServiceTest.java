package com.zkrypto.zkMatch.corporation;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.*;
import com.zkrypto.zkMatch.domain.corporation.application.dto.response.*;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.corporation.domain.repository.CorporationRepository;
import com.zkrypto.zkMatch.domain.member.application.dto.request.ResumeCreationCommand;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.UpdateApplierStatusCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.CorporationPostResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostApplierResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import com.zkrypto.zkMatch.global.crypto.SaltUtil;
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
import java.util.UUID;

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

    @Autowired
    private RecruitRepository recruitRepository;

    @Autowired
    MemberService memberService;

    @Test
    void 생성_테스트() {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "test");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "test");
        ReflectionUtil.setter(corporationCreationCommand, "password", "test");
        corporationService.createCorporation(corporationCreationCommand, null);

        // member 생성 테스트
        boolean memberTest = memberRepository.existsByLoginId("test");
        assertThat(memberTest).isTrue();

        // 기업 생성 테스트
        boolean corporationTest = corporationRepository.existsCorporationByCorporationName("test");
        assertThat(corporationTest).isTrue();
    }

    @Test
    void 조회_테스트() {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "test");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "test");
        ReflectionUtil.setter(corporationCreationCommand, "password", "test");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 기업 조회
        Member member = memberRepository.findMemberByLoginId("test").get();
        CorporationResponse corporation = corporationService.getCorporation(member.getMemberId());

        // 조회 테스트
        assertThat(corporation.getCorporationName()).isEqualTo("test");
    }

    @Test
    void 공고_생성_테스트() {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "test");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "test");
        ReflectionUtil.setter(corporationCreationCommand, "password", "test");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 멤버 조회
        Member member = memberRepository.findMemberByLoginId("test").get();

        // 공고 생성
        corporationService.createPost(member.getMemberId(), new PostCreationCommand());

        // 생성 테스트
        List<Post> posts = postRepository.findPostByCorporation(member.getCorporation());
        assertThat(posts.size()).isEqualTo(1);
    }

    @Test
    void 공고_업데이트_테스트() {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "test");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "test");
        ReflectionUtil.setter(corporationCreationCommand, "password", "test");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 멤버 조회
        Member member = memberRepository.findMemberByLoginId("test").get();

        // 공고 생성
        PostCreationCommand postCreationCommand = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand, "title", "지크립토");
        ReflectionUtil.setter(postCreationCommand, "content", "테스트");
        corporationService.createPost(member.getMemberId(), postCreationCommand);

        //공고 조회
        Post findPost = postRepository.findPostByTitle("지크립토").get();

        // 공고 수정
        PostUpdateCommand postUpdateCommand = new PostUpdateCommand();
        ReflectionUtil.setter(postUpdateCommand, "title", "지크립토");
        ReflectionUtil.setter(postUpdateCommand, "content", "테스트2");
        corporationService.updatePost(findPost.getPostId().toString(), postUpdateCommand);

        // 검증
        List<Post> myPosts = postRepository.findPostByCorporation(member.getCorporation());
        assertThat(myPosts.get(0).getContent()).isEqualTo("테스트2");
        assertThat(myPosts.get(0).getTitle()).isEqualTo("지크립토");
    }

    @Test
    void 공고_조회_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        List<CorporationPostResponse> post = corporationService.getCorporationPost(admin.getMemberId(), "");

        // 검증
        assertThat(post.get(0).getTitle()).isEqualTo("hi");
    }

    @Test
    void 공고_조회_키워드_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "[지크립토] 개발자 채용");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        PostCreationCommand postCreationCommand2 = new PostCreationCommand();
        ReflectionUtil.setter(postCreationCommand2, "title", "[지크립토] 디자이너 채용");
        ReflectionUtil.setter(postCreationCommand2, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand2, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand2);

        // 멤버 생성
        Member member = new Member();
        memberRepository.save(member);

        // 공고 조회
        List<CorporationPostResponse> post = corporationService.getCorporationPost(admin.getMemberId(), "개발자");

        // 검증
        assertThat(post.size()).isEqualTo(1);
        assertThat(post.get(0).getTitle()).isEqualTo("[지크립토] 개발자 채용");
    }

    @Test
    void 공고_지원자_조회_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "name", "kim");
        ReflectionUtil.setter(member, "email", "lmkn5342@gmail.com");
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 지원
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 지원자 상세 조회
        ApplierDetailResponse applierDetail = corporationService.getApplierDetail(recruit.getId().toString());

        // 증명
        assertThat(applierDetail.getName()).isEqualTo("k00");
    }

    @Test
    void 공고_합격_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "email", "lmkn5342@gmail.com");
        ReflectionUtil.setter(member, "name", "김동현");
        memberRepository.save(member);

        // 공고 조회
        Post post = postRepository.getPostByTitle("hi").get();

        // 공고 지원
        postService.applyPost(member.getMemberId(), post.getPostId().toString());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(post.getPostId(), member).get();

        // 지원자 합격
        UpdateApplierStatusCommand updateApplierStatusCommand = new UpdateApplierStatusCommand();
        ReflectionUtil.setter(updateApplierStatusCommand, "status", Status.PASS);
        corporationService.updateApplierStatus(recruit.getId().toString(), updateApplierStatusCommand);

        // 검증
        List<PostApplierResponse> postApplier = corporationService.getPostApplier(post.getPostId().toString());
        assertThat(postApplier.get(0).getStatus()).isEqualTo("합격");
    }

    @Test
    void 공고_탈락_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "email", "lmkn5342@gmail.com");
        ReflectionUtil.setter(member, "name", "김동현");
        memberRepository.save(member);

        // 공고 조회
        Post post = postRepository.getPostByTitle("hi").get();


        // 공고 지원
        postService.applyPost(member.getMemberId(), post.getPostId().toString());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(post.getPostId(), member).get();

        // 지원자 합격
        UpdateApplierStatusCommand updateApplierStatusCommand = new UpdateApplierStatusCommand();
        ReflectionUtil.setter(updateApplierStatusCommand, "status", Status.FAILED);
        corporationService.updateApplierStatus(recruit.getId().toString(), updateApplierStatusCommand);

        // 검증
        List<PostApplierResponse> postApplier = corporationService.getPostApplier(post.getPostId().toString());
        assertThat(postApplier.get(0).getStatus()).isEqualTo("불합격");
    }

    @Test
    void 공고_업데이트_테스트_실패() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "email", "lmkn5342@gmail.com");
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 지원
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 지원자 합격
        UpdateApplierStatusCommand updateApplierStatusCommand = new UpdateApplierStatusCommand();
        ReflectionUtil.setter(updateApplierStatusCommand, "status", Status.INTERVIEW);

        // 검증
        Assertions.assertThatThrownBy(() -> {
            corporationService.updateApplierStatus(recruit.getId().toString(), updateApplierStatusCommand);
        }).hasMessageContaining("허용되지 않은 status 입니다.");
    }

    @Test
    void 공고_인터뷰_생성_테스트() {
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
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 면접 생성
        InterviewCreationCommand interviewCreationCommand = new InterviewCreationCommand();
        ReflectionUtil.setter(interviewCreationCommand, "title", "1차면접");
        corporationService.createInterview(recruit.getId().toString(), interviewCreationCommand);

        // 검증
        Recruit result = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();
        assertThat(result.getInterview().getTitle()).isEqualTo("1차면접");
        assertThat(result.getStatus()).isEqualTo(Status.INTERVIEW);
    }

    @Test
    void 공고_인터뷰_조회_테스트() {
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
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 면접 생성
        InterviewCreationCommand interviewCreationCommand = new InterviewCreationCommand();
        ReflectionUtil.setter(interviewCreationCommand, "title", "1차면접");
        ReflectionUtil.setter(interviewCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(interviewCreationCommand, "endDate", LocalDateTime.of(2025, 1,1, 1, 1));
        corporationService.createInterview(recruit.getId().toString(), interviewCreationCommand);

        // 검증
        InterviewResponse interview = corporationService.getInterview(recruit.getId().toString());
        Assertions.assertThat(interview).isNotNull();
        Assertions.assertThat(interview.getInterviewName()).isEqualTo("1차면접");
    }

    @Test
    void 공고_인터뷰_실패_테스트() {
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
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 지원자 합격
        UpdateApplierStatusCommand updateApplierStatusCommand = new UpdateApplierStatusCommand();
        ReflectionUtil.setter(updateApplierStatusCommand, "status", Status.PASS);
        corporationService.updateApplierStatus(recruit.getId().toString(), updateApplierStatusCommand);

        // 면접 생성
        InterviewCreationCommand interviewCreationCommand = new InterviewCreationCommand();
        ReflectionUtil.setter(interviewCreationCommand, "title", "1차면접");
        Assertions.assertThatThrownBy(() -> {
            corporationService.createInterview(recruit.getId().toString(), interviewCreationCommand);
        }).hasMessage("면접 대상이 아닙니다.");
    }

    @Test
    void 공고_인터뷰_수정_테스트() {
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
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 면접 생성
        InterviewCreationCommand interviewCreationCommand = new InterviewCreationCommand();
        ReflectionUtil.setter(interviewCreationCommand, "title", "1차면접");
        ReflectionUtil.setter(interviewCreationCommand, "location", "지크립토");
        corporationService.createInterview(recruit.getId().toString(), interviewCreationCommand);

        // 면접 수정
        InterviewUpdateCommand interviewUpdateCommand = new InterviewUpdateCommand();
        ReflectionUtil.setter(interviewUpdateCommand, "title", "1차면접");
        ReflectionUtil.setter(interviewUpdateCommand, "location", "한양대");
        corporationService.updateInterview(recruit.getId().toString(), interviewUpdateCommand);

        // 검증
        Recruit result = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();
        assertThat(result.getInterview().getTitle()).isEqualTo("1차면접");
        assertThat(result.getInterview().getLocation()).isEqualTo("한양대");
        assertThat(result.getStatus()).isEqualTo(Status.INTERVIEW);
    }

    @Test
    void 지원자_평가_테스트() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "email", "lmkn5342@gmail.com");
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 지원
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 지원자 합격
        UpdateApplierStatusCommand updateApplierStatusCommand = new UpdateApplierStatusCommand();
        ReflectionUtil.setter(updateApplierStatusCommand, "status", Status.PASS);
        corporationService.updateApplierStatus(recruit.getId().toString(), updateApplierStatusCommand);

        // 지원자 평가
        EvaluationCreationCommand evaluationCreationCommand = new EvaluationCreationCommand();
        ReflectionUtil.setter(evaluationCreationCommand, "evaluation", "good");
        corporationService.evaluateApplier(recruit.getId().toString(), evaluationCreationCommand);

        // 검증
        List<EvaluationResponse> evaluation = corporationService.getEvaluation(recruit.getId().toString());
        Assertions.assertThat(evaluation.size()).isEqualTo(1);
        Assertions.assertThat(evaluation.get(0).getEvaluation()).isEqualTo("good");
    }

    @Test
    void 지원자_평가_테스트_실패() {
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
        ReflectionUtil.setter(postCreationCommand, "title", "hi");
        ReflectionUtil.setter(postCreationCommand, "startDate", LocalDateTime.of(2025, 1,1, 1, 1));
        ReflectionUtil.setter(postCreationCommand, "endDate", LocalDateTime.of(2026, 1,1, 1, 1));
        corporationService.createPost(admin.getMemberId(), postCreationCommand);

        // 멤버 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "email", "lmkn5342@gmail.com");
        memberRepository.save(member);

        // 공고 조회
        List<PostResponse> posts = postService.getPost();

        // 공고 지원
        postService.applyPost(member.getMemberId(), posts.get(0).getPostId());

        // 지원 내역 조회
        Recruit recruit = recruitRepository.findRecruitByPostAndMember(UUID.fromString(posts.get(0).getPostId()), member).get();

        // 지원자 평가
        EvaluationCreationCommand evaluationCreationCommand = new EvaluationCreationCommand();
        ReflectionUtil.setter(evaluationCreationCommand, "evaluation", "good");
        
        Assertions.assertThatThrownBy(() -> corporationService.evaluateApplier(recruit.getId().toString(), evaluationCreationCommand))
                .hasMessageContaining("평가 대상이 아닙니다.");
    }

    @Test
    void 인재_검색_테스트() {
        // 멤버1 생성
        Member member1 = new Member();
        ReflectionUtil.setter(member1, "salt", SaltUtil.generateSalt());
        ReflectionUtil.setter(member1, "name", "김동현");
        memberRepository.save(member1);

        // 이력서 생성
        String member1_data1 = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"univ\": \"한양대학교\",\n" +
                "    \"univType\": \"4년제\",\n" +
                "    \"maj\": \"정보시스템\",\n" +
                "    \"degree\": \"학사\",\n" +
                "    \"registerNumber\": \"hyu-20251234125\"\n" +
                "}";
        ResumeCreationCommand command = new ResumeCreationCommand();
        ReflectionUtil.setter(command, "resumeType", ResumeType.EDUCATION);
        ReflectionUtil.setter(command, "data", member1_data1);
        memberService.createMemberResume(member1.getMemberId(), command);

        String member1_data2 = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"pid\": \"002000-000000\",\n" +
                "    \"license\": \"정보처리기사\",\n" +
                "    \"expired\": \"20020306\"\n" +
                "}";
        ResumeCreationCommand command1 = new ResumeCreationCommand();
        ReflectionUtil.setter(command1, "resumeType", ResumeType.LICENSE);
        ReflectionUtil.setter(command1, "data", member1_data2);
        memberService.createMemberResume(member1.getMemberId(), command1);

        // 멤버2 생성
        Member member2 = new Member();
        ReflectionUtil.setter(member2, "salt", SaltUtil.generateSalt());
        ReflectionUtil.setter(member2, "name", "김동욱");
        memberRepository.save(member2);

        // 이력서 생성
        String member2_data1 = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동욱\",\n" +
                "    \"univ\": \"짭한양대\",\n" +
                "    \"univType\": \"2년제\",\n" +
                "    \"maj\": \"정보시스템\",\n" +
                "    \"degree\": \"학사\",\n" +
                "    \"registerNumber\": \"hyu-20251234125\"\n" +
                "}";
        ResumeCreationCommand command2 = new ResumeCreationCommand();
        ReflectionUtil.setter(command2, "resumeType", ResumeType.EDUCATION);
        ReflectionUtil.setter(command2, "data", member2_data1);
        memberService.createMemberResume(member2.getMemberId(), command);

        // 필터링
        List<CandidateResponse> response1 = corporationService.searchCandidate(List.of("정보처리기사"), 0, "4년제");
        List<CandidateResponse> response2 = corporationService.searchCandidate(null, 0, "2년제");
        List<CandidateResponse> response3 = corporationService.searchCandidate(List.of("정보처리기사"), 2, "4년제");

        // 검증
        assertThat(response1.size()).isEqualTo(1);
        assertThat(response1.get(0).getUserId()).isEqualTo(member1.getMemberId().toString());

        assertThat(response2.size()).isEqualTo(2);

        assertThat(response3.size()).isEqualTo(0);
    }
}
