package com.zkrypto.zkMatch.domain.member.application.dto.response;

import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberResumeResponse {
    private ResumeType resumeType;
    private List<Object> data;
    private String did;
}
