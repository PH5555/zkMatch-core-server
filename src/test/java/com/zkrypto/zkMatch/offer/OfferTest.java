package com.zkrypto.zkMatch.offer;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CandidateOfferCommand;
import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberOfferResponse;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.global.utils.ReflectionUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
public class OfferTest {
    @Autowired
    CorporationService corporationService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    void 채용_제안_테스트() {
        // 기업 생성
        CorporationCreationCommand corporationCreationCommand = new CorporationCreationCommand();
        ReflectionUtil.setter(corporationCreationCommand, "corporationName", "지크립토");
        ReflectionUtil.setter(corporationCreationCommand, "loginId", "1234");
        ReflectionUtil.setter(corporationCreationCommand, "password", "1234");

        corporationService.createCorporation(corporationCreationCommand, null);

        // 멤버 조회
        Member manager = memberRepository.findMemberByLoginId("1234").get();

        // 유저 생성
        Member member = new Member();
        ReflectionUtil.setter(member, "email", "lmkn5342@gmail.com");
        memberRepository.save(member);

        // 채용 제안
        CandidateOfferCommand candidateOfferCommand = new CandidateOfferCommand();
        ReflectionUtil.setter(candidateOfferCommand, "memberId", member.getMemberId().toString());
        corporationService.offerCandidate(manager.getMemberId(), candidateOfferCommand);

        // 채용 제안 조회
        List<MemberOfferResponse> offers = memberService.getMemberOffer(member.getMemberId());

        // 검증
        Assertions.assertThat(offers.size()).isEqualTo(1);
        Assertions.assertThat(offers.get(0).getCorporationName()).isEqualTo("지크립토");
    }
}
