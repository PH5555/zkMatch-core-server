package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class PostApplierResponse {
    private String recruitId;
    private String applierName;
    private String applierPortfolio;
    private String applyDate;
    private String status;

    private PostApplierResponse(String recruitId, String applierName, String applierPortfolio, String applyDate, Status status) {
        this.recruitId = recruitId;
        this.applierName = applierName;
        this.applierPortfolio = applierPortfolio;
        this.applyDate = applyDate;
        this.status = statusFormatter(status);
    }

    private String statusFormatter(Status status) {
        if(status == Status.PASS) {
            return "합격";
        }
        if(status == Status.PENDING) {
            return "검토 중";
        }
        if(status == Status.FAILED) {
            return "불합격";
        }
        if(status == Status.INTERVIEW) {
            return "면접";
        }
        return "";
    }

    public static PostApplierResponse from(Recruit recruit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String date = recruit.getCreatedAt().format(formatter);
        return new PostApplierResponse(recruit.getId().toString(), recruit.getMember().getName(), recruit.getMember().getPortfolioUrl(), date, recruit.getStatus());
    }
}
