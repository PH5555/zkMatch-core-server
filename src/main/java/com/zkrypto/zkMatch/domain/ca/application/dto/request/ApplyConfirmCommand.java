package com.zkrypto.zkMatch.domain.ca.application.dto.request;

import lombok.Getter;

@Getter
public class ApplyConfirmCommand {
    private String applicationId;
    private String proof;
    private String[] value;
}
