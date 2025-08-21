package com.zkrypto.zkMatch.domain.ca.application.dto.request;

import lombok.Getter;

@Getter
public class ApplyConfirmCommand {
    private String memberId;
    private String proof;
    private String vk;
    private String[] value;
}
