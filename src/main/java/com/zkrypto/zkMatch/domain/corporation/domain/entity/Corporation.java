package com.zkrypto.zkMatch.domain.corporation.domain.entity;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.CorporationCreationCommand;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Corporation {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID corporationId;

    private String corporationName;
    private String corporationType;
    private String corporationUrl;
    private String registerFile;
    private String corporationAddress;
    private String industryType;

    @OneToOne(mappedBy = "corporation", cascade = CascadeType.PERSIST)
    private Member member;

    private Corporation(String corporationName, String corporationType, String corporationUrl, String registerFile, String industryType, String corporationAddress, Member member) {
        this.corporationName = corporationName;
        this.corporationAddress = corporationAddress;
        this.member = member;
        this.corporationType = corporationType;
        this.corporationUrl = corporationUrl;
        this.registerFile = registerFile;
        this.industryType = industryType;
    }

    public static Corporation from(CorporationCreationCommand command, String registerFileUrl, Member member) {
        Corporation corporation = new Corporation(
                command.getCorporationName(),
                command.getCorporationType(),
                command.getCorporationUrl(),
                registerFileUrl,
                command.getIndustryType(),
                command.getCorporationAddress(), member);
        member.setCorporation(corporation);
        return corporation;
    }
}
