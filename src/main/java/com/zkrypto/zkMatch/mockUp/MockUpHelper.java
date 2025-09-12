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
        createMockMembers();
        createCorporationAccount();
        createPost();
//        apply();
    }

    private void createMockMembers() {
        // 목업 데이터 리스트
        List<String> names = List.of("김동현", "이지은", "박서준", "최수빈", "정다현", "강민준", "윤채원", "한지민", "송예준", "안서연", "황도윤");
        List<String> loginIds = List.of("1234", "leejieun", "parkseojun", "choisubin", "jungdahyun", "kangminjun", "yoonchaewon", "hanjimin", "songyejun", "anseoyeon", "hwangdoyun");
        List<String> emails = List.of("lmkn5342@naver.com","jieun.dev@gmail.com", "seojun.p@naver.com", "subin.choi@kakao.com", "dahyun.j@gmail.com", "minjun.kang@naver.com", "chaewon.y@gmail.com", "jimin.han@kakao.com", "yejun.song@naver.com", "seoyeon.an@gmail.com", "doyun.hwang@kakao.com");
        List<String> phoneNumbers = List.of("010-1234-4234","010-1234-5678", "010-2345-6789", "010-3456-7890", "010-4567-8901", "010-5678-9012", "010-6789-0123", "010-7890-1234", "010-8901-2345", "010-9012-3456", "010-0123-4567");
        List<String> births = List.of("020202","950516", "881216", "001205", "980528", "970810", "010811", "820111", "030315", "991104", "960722");

        List<List<String>> interestList = List.of(
                List.of("백엔드", "블록체인"), List.of("백엔드", "클라우드"), List.of("프론트엔드", "UX/UI"),
                List.of("AI", "데이터 분석"), List.of("블록체인", "보안"),
                List.of("데브옵스", "인프라"), List.of("iOS", "모바일"),
                List.of("안드로이드", "모바일"), List.of("게임 개발", "Unity"),
                List.of("백엔드", "데이터베이스"), List.of("프론트엔드", "Vue.js")
        );

        for (int i = 0; i < 10; i++) {
            SignUpCommand signUpCommand = new SignUpCommand();
            String ci = "testCi" + (i + 1);

            ReflectionUtil.setter(signUpCommand, "ci", ci);
            ReflectionUtil.setter(signUpCommand, "loginId", loginIds.get(i));
            ReflectionUtil.setter(signUpCommand, "password", "1234"); // 공통 비밀번호 사용
            ReflectionUtil.setter(signUpCommand, "email", emails.get(i));
            ReflectionUtil.setter(signUpCommand, "emailAuthNumber", "test");
            ReflectionUtil.setter(signUpCommand, "name", names.get(i));
            ReflectionUtil.setter(signUpCommand, "phoneNumber", phoneNumbers.get(i));
            ReflectionUtil.setter(signUpCommand, "birth", births.get(i));
            ReflectionUtil.setter(signUpCommand, "interests", interestList.get(i));

            String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
            Member member = Member.from(signUpCommand, hashedPassword);
            memberRepository.save(member);
        }
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
        ReflectionUtil.setter(postCreationCommand, "content", "저희 회사는 웹 3.0 시대를 선도하고 있습니다. 자유로운 분위기 속에서 최고의 동료들과 함께 성장하며 기술로 세상을 바꾸는 여정에 동참할 열정적인 개발자분을 기다립니다. \n\n담당하시게 될 주요 업무\n" +
                "- 블록체인 기반 서비스의 백엔드 시스템 설계 및 개발 (Node.js, TypeScript)\n" +
                "-Solidity 기반의 스마트 컨트랙트 설계, 개발 및 배포\n" +
                "- 블록체인 노드(EVM 호환)와의 연동 및 데이터 처리 API 개발\n" +
                "- IPFS 등 분산 스토리지 시스템을 활용한 데이터 관리\n" +
                "- 서비스 안정성 및 확장성을 고려한 시스템 아키텍처 설계 및 개선\n\n 이런 경험이 있다면 더욱 좋습니다 (우대 사항)\n" +
                "- NestJS, Express 등 Node.js 프레임워크 사용 경험\n" +
                "- Docker, Kubernetes 등 컨테이너 기술 활용 경험\n" +
                "- AWS, GCP 등 클라우드 플랫폼 기반 서비스 구축 및 운영 경험\n" +
                "- 대규모 트래픽 처리 및 성능 최적화 경험\n" +
                "- 오픈소스 프로젝트 참여 또는 기여 경험\n" +
                "- 블록체인 관련 커뮤니티 활동 및 최신 기술 동향에 대한 높은 관심" );
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
        ReflectionUtil.setter(postCreationCommand, "postType", PostType.FREELANCER);

        Member admin = memberRepository.findMemberByLoginId("admin").get();
        corporationService.createPost(admin.getMemberId(), postCreationCommand);
    }

    private void apply() {
        Member member = memberRepository.findMemberByLoginId("1234").get();
        Post post = postRepository.findPostByTitle("지크립토 백엔드 개발자 채용").get();
        postService.applyPost(member.getMemberId(), post.getPostId().toString());
    }
}
