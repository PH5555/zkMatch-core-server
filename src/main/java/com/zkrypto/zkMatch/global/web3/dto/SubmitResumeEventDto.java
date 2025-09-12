package com.zkrypto.zkMatch.global.web3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmitResumeEventDto {
    private String userId;
    private String resumeId;
    private String data;
}
