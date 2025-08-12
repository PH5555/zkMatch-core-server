package com.zkrypto.zkMatch.domain.member.application.dto.request;

import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import lombok.Getter;

@Getter
public class ResumeCreationCommand {
    private ResumeType resumeType;
    private String data;
}
