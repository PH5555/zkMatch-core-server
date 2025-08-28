package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.domain.application.domain.entity.Application;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.global.utils.BaseMultibaseUtil;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import com.zkrypto.zkMatch.global.utils.JsonUtil;
import com.zkrypto.zkMatch.global.utils.MultiBaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Getter
public class ApplyQrResponse {
    private String payload;
    private String payloadType;
    private String validUntil;

    @Setter
    private byte[] qrImage;

    public ApplyQrResponse(String payload, String payloadType, String validUntil) {
        this.payload = payload;
        this.payloadType = payloadType;
        this.validUntil = validUntil;
    }

    public static ApplyQrResponse from(Application application) {
        String jsonString = JsonUtil.serializeAndSort(new RequestApplyDto(
                application.getApplicationId().toString(),
                application.getPost().getMajorRequirement(),
                "",
                "",
                application.getPost().getEducationRequirement(),
                "",
                String.valueOf(application.getCreatedAt().toEpochSecond(ZoneOffset.UTC)),
                String.valueOf(LocalDate.of(1970, 1, 1).plusYears(application.getPost().getExperienceRequirement()).atStartOfDay().toEpochSecond(ZoneOffset.UTC)),
                application.getPost().getLicenseRequirement().getFirst()
                ));
        String encDataPayload = BaseMultibaseUtil.encode(jsonString.getBytes(), MultiBaseType.base64);
        return new ApplyQrResponse(encDataPayload, "APPLY", DateFormatter.format(application.getValidTime()));
    }

    @AllArgsConstructor
    @Getter
    public static class RequestApplyDto {
        private String applicationId;
        private String major1;
        private String major2;
        private String major3;
        private String univType1;
        private String univType2;
        private String currentTime;
        private String employPeriod;
        private String license;
    }
}
