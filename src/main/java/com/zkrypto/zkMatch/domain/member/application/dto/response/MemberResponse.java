package com.zkrypto.zkMatch.domain.member.application.dto.response;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private String name;
    private String email;
    private String phoneNumber;
    private String portfolioUrl;
    private String portfolioName;

    public MemberResponse(String name, String email, String phoneNumber, String portfolioUrl, String portfolioName) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.portfolioUrl = portfolioUrl;
        this.portfolioName = portfolioName;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getName(), member.getEmail(), member.getPhoneNumber(), member.getPortfolioUrl(), member.getPortfolioName());
    }
}
