package com.zkrypto.zkMatch.domain.portfolio.domain.entity;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Portfolio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String personalStatement;
}
