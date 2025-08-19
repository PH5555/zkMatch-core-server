package com.zkrypto.zkMatch.domain.corporation.application.dto.response;

import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResumeResponse;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import lombok.Getter;

import java.util.List;

@Getter
public class ApplierDetailResponse {
    private String name;
    private String applyDate;
    private String email;
    private String applyPosition;
    private Status status;
    private String portfolioUrl;
    private String phoneNumber;
    private List<MemberResumeResponse> resumes;

    public ApplierDetailResponse(String name, String applyDate, String email, String applyPosition, Status status, String portfolioUrl, String phoneNumber, List<MemberResumeResponse> resumes) {
        this.name = name;
        this.applyDate = applyDate;
        this.email = email;
        this.applyPosition = applyPosition;
        this.status = status;
        this.portfolioUrl = portfolioUrl;
        this.phoneNumber = phoneNumber;
        this.resumes = resumes;
    }

    public static ApplierDetailResponse from(Recruit recruit, List<MemberResumeResponse> resumes) {
        return new ApplierDetailResponse(
                recruit.getMember().getName(),
                DateFormatter.format(recruit.getCreatedAt()),
                recruit.getMember().getEmail(),
                recruit.getPost().getContent(),
                recruit.getStatus(),
                recruit.getMember().getPortfolioUrl(),
                recruit.getMember().getPhoneNumber(),
                resumes
        );
    }
}
