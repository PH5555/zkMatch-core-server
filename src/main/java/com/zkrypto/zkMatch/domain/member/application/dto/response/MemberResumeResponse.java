package com.zkrypto.zkMatch.domain.member.application.dto.response;

import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberResumeResponse {
    private Long resumeId;
    private ResumeType resumeType;
    private Object data;
    private Boolean did;
}
