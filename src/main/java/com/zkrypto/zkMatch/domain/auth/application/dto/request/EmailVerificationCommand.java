package com.zkrypto.zkMatch.domain.auth.application.dto.request;

import lombok.Getter;

@Getter
public class EmailVerificationCommand {
    private String ci;
    private String email;
}
