package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.snark.SNARK;
import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;
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
    private final KeyRedisService keyRedisService;
    private final RedisService redisService;

    public KeyPair getKey(String keyId) {
        return keyRedisService.getKeyPair(keyId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ISSUER_KEY));
    }

    public void confirmApply(ApplyConfirmCommand command) {
        boolean verify = SNARK.verify(command.getVk(), command.getProof(),
                this.getKey("assert").getPublicKey(),
                this.getKey("assert").getPublicKey(),
                this.getKey("assert").getPublicKey(),
                command.getValue()[0],
                command.getValue()[1],
                command.getValue()[2],
                command.getValue()[3],
                command.getValue()[4],
                command.getValue()[5],
                command.getValue()[6],
                command.getValue()[7]
        );

        if(verify) {
            redisService.setData(command.getMemberId(), "SUCCESS", 600000L);
        }
    }
}
