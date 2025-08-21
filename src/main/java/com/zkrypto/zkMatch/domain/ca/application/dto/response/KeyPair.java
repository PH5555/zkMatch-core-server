package com.zkrypto.zkMatch.domain.ca.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyPair {
    private String privateKey;
    private String publicKey;
}