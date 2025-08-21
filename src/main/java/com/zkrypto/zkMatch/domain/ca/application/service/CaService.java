package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.snark.SNARK;
import com.zkrypto.zkMatch.domain.ca.application.domain.constant.KeyPair;
import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;
import com.zkrypto.zkMatch.domain.ca.application.dto.response.PkResponse;
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

    public PkResponse getPk() {
        KeyPair keyPair = keyRedisService.getKeyPair("assert").orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ISSUER_KEY));
        return new PkResponse(keyPair.getPublicKey());
    }

    public void confirmApply(ApplyConfirmCommand command) {
        boolean verify = SNARK.verify(command.getVk(), command.getProof(),
                this.getPk().getPk(),
                this.getPk().getPk(),
                this.getPk().getPk(),
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
