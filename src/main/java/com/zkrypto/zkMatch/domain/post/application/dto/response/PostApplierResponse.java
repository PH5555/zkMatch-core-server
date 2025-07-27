package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import lombok.Getter;

@Getter
public class PostApplierResponse {
    private String applierId;
    private String applierName;
    private String applierPersonalStatement;
    private Status status;

    private PostApplierResponse(String applierId, String applierName, String applierPersonalStatement, Status status) {
        this.applierId = applierId;
        this.applierName = applierName;
        this.applierPersonalStatement = applierPersonalStatement;
        this.status = status;
    }

    public static PostApplierResponse from(Recruit recruit) {
        return new PostApplierResponse(recruit.getMember().getMemberId().toString(), recruit.getMember().getName(), recruit.getMember().getPersonalStatement(), recruit.getStatus());
    }
}
