package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.zkMatch.domain.ca.application.dto.response.KeyPair;
import com.zkrypto.zkMatch.global.redis.KeyRedisService;
import com.zkrypto.zkMatch.global.redis.RedisService;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaService {
    private final KeyRedisService redisService;

    public KeyPair getKey(String keyId) {
        return redisService.getKeyPair(keyId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ISSUER_KEY));
    }
}
