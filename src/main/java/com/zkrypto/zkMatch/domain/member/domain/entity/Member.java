package com.zkrypto.zkMatch.domain.member.domain.entity;

import com.zkrypto.zkMatch.domain.auth.application.dto.request.SignUpCommand;
import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.member.domain.constant.Role;
import com.zkrypto.zkMatch.domain.portfolio.domain.entity.Portfolio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID memberId;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String loginId;
    private String password;
    private String name;
    private String birth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String portfolioUrl;
    private String interests;
    private String refreshToken;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id")
    private Corporation corporation;

    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private Member(Role role, String loginId, String password) {
        this.role = role;
        this.loginId = loginId;
        this.password = password;
    }

    private Member(Role role, String loginId, String password, String name, String email, String phoneNumber) {
        this.role = role;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void signOut() {
        this.refreshToken = null;
    }

    public void storeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static Member from(SignUpCommand signUpCommand, String hashedPassword) {
        Member member = new Member(Role.ROLE_USER, signUpCommand.getLoginId(), hashedPassword);
        member.setPortfolio(new Portfolio());
        return member;
    }

    public static Member from(CorporationCreationCommand corporationCreationCommand, String hashedPassword) {
        return new Member(Role.ROLE_ADMIN, corporationCreationCommand.getLoginId(), hashedPassword, corporationCreationCommand.getName(), corporationCreationCommand.getEmail(), corporationCreationCommand.getPhoneNumber());
    }
}
