package com.zkrypto.zkMatch.domain.ca.application.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyPair {
    private String privateKey;
    private String publicKey;
}