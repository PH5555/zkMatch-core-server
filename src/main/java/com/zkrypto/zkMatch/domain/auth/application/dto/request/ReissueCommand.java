package com.zkrypto.zkMatch.domain.auth.application.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ReissueCommand {
    private String refreshToken;
    private UUID memberId;
}
