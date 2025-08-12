package com.zkrypto.zkMatch.domain.offer.domain.entity;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Offer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id")
    private Corporation corporation;

    private LocalDateTime createdAt;

    public Offer(Member member, Corporation corporation) {
        this.member = member;
        this.corporation = corporation;
        this.createdAt = LocalDateTime.now();
    }
}
