package com.zkrypto.zkMatch.global.web3.dto;

import com.zkrypto.zkMatch.global.web3.ApplicationContract;
import com.zkrypto.zkMatch.global.web3.Web3Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class ApplicationEventDto {
    private String userId;
    private String postId;
    private String proof;
}
