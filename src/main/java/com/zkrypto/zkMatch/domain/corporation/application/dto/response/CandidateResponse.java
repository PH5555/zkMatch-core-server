package com.zkrypto.zkMatch.domain.corporation.application.dto.response;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import lombok.Getter;

@Getter
public class CandidateResponse {
    private String userId;
    private String name;
    private String rating;
    private String projectCnt;

    private CandidateResponse(String userId, String name, String rating, String projectCnt) {
        this.userId = userId;
        this.name = name;
        this.rating = rating;
        this.projectCnt = projectCnt;
    }

    public static CandidateResponse from(Member member, Double rating, Long count) {
        return new CandidateResponse(member.getMemberId().toString(), member.getName().charAt(0) + "00", rating.toString(), count.toString());
    }
}
