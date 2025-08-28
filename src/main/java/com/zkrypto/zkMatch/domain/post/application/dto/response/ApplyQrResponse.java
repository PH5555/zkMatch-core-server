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
import java.util.List;
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
                application.getPost().getEducationRequirement(),
                application.getPost().getLicenseRequirement(),
                application.getPost().getExperienceRequirement()
        ));
        String encDataPayload = BaseMultibaseUtil.encode(jsonString.getBytes(), MultiBaseType.base64);
        return new ApplyQrResponse(encDataPayload, "APPLY", DateFormatter.format(application.getValidTime()));
    }

    @AllArgsConstructor
    @Getter
    public static class RequestApplyDto {
        private String applicationId;
        private String majorRequirement;
        private String educationRequirement;
        private List<String> licenseRequirement;
        private int experienceRequirement;
    }
}
