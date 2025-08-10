package com.zkrypto.zkMatch.domain.member.domain.entity;

import com.zkrypto.zkMatch.domain.auth.application.dto.request.SignUpCommand;
import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.member.domain.constant.Role;
import com.zkrypto.zkMatch.global.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    private String ci;

    @Setter
    private String portfolioUrl;

    @Convert(converter = StringListConverter.class)
    private List<String> interests;
    private String refreshToken;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id")
    private Corporation corporation;

    public Member(Role role, String loginId, String password, String name, String birth, String gender, String email, String phoneNumber, List<String> interests, String ci) {
        this.role = role;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.interests = interests;
        this.ci = ci;
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
        return new Member(
                Role.ROLE_USER,
                signUpCommand.getLoginId(),
                hashedPassword,
                signUpCommand.getName(),
                signUpCommand.getBirth(),
                signUpCommand.getGender(),
                signUpCommand.getEmail(),
                signUpCommand.getPhoneNumber(),
                signUpCommand.getInterests(),
                signUpCommand.getCi()
        );
    }

    public static Member from(CorporationCreationCommand corporationCreationCommand, String hashedPassword) {
        return new Member(Role.ROLE_ADMIN, corporationCreationCommand.getLoginId(), hashedPassword, corporationCreationCommand.getName(), corporationCreationCommand.getEmail(), corporationCreationCommand.getPhoneNumber());
    }
}
