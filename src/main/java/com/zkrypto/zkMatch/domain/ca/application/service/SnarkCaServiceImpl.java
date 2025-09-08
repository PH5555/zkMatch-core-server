package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.snark.SNARK;
import com.zkrypto.zkMatch.domain.application.domain.entity.Application;
import com.zkrypto.zkMatch.domain.application.domain.repository.ApplicationRepository;
import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class SnarkCaServiceImpl implements CaService {
    private final ApplicationRepository applicationRepository;

    @Value("classpath:vk.bin")
    private Resource vkResource;

    @Override
    @Transactional
    public void confirmApply(ApplyConfirmCommand command) {
        // 지원 정보 조회
        Application application = applicationRepository.findById(Long.parseLong(command.getApplicationId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPLICATION));

        String vk = getVerifyKey();

        // 영지식 증명
        boolean verify = SNARK.verify(
                vk,
                command.getProof(),
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

    public String getVerifyKey() {
        try (var inputStream = vkResource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
