package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.snark.SNARK;
import com.zkrypto.zkMatch.domain.application.domain.entity.Application;
import com.zkrypto.zkMatch.domain.application.domain.repository.ApplicationRepository;
import com.zkrypto.zkMatch.domain.ca.application.domain.constant.KeyPair;
import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;
import com.zkrypto.zkMatch.domain.ca.application.dto.response.PkResponse;
import com.zkrypto.zkMatch.global.redis.KeyRedisService;
import com.zkrypto.zkMatch.global.redis.RedisService;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CaService {
    private final KeyRedisService keyRedisService;
    private final ApplicationRepository applicationRepository;

    public PkResponse getPk() {
        KeyPair keyPair = keyRedisService.getKeyPair("assert").orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ISSUER_KEY));
        return new PkResponse(keyPair.getPublicKey());
    }

    @Transactional
    public void confirmApply(ApplyConfirmCommand command) {
        // 지원 정보 조회
        Application application = applicationRepository.findById(Long.parseLong(command.getApplicationId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPLICATION));

        String vk = "";

        // 영지식 증명
        boolean verify = SNARK.verify(
                vk,
                command.getProof(),
                this.getPk().getPk(),
                this.getPk().getPk(),
                this.getPk().getPk(),
                application.getPost().getMajorRequirement(),
                "",
                "",
                application.getPost().getEducationRequirement(),
                "",
                String.valueOf(application.getCreatedAt().toEpochSecond(ZoneOffset.UTC)),
                String.valueOf(LocalDate.of(1970, 1, 1).plusYears(application.getPost().getExperienceRequirement()).atStartOfDay().toEpochSecond(ZoneOffset.UTC)),
                application.getPost().getLicenseRequirement().getFirst()
        );

        // 증명이 되면 success로 상태 변환
        if(verify) {
            application.success();
        }
    }
}
