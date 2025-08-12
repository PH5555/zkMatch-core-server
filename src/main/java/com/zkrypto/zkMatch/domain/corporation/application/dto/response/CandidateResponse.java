package com.zkrypto.zkMatch.domain.corporation.application.dto.response;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import lombok.Getter;

@Getter
public class CandidateResponse {
    private String userId;
    private String name;

    private CandidateResponse(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public static CandidateResponse from(Member member) {
        return new CandidateResponse(member.getMemberId().toString(), member.getName().charAt(0) + "00");
    }
}
