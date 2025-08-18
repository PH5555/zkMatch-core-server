package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.global.utils.BaseMultibaseUtil;
import com.zkrypto.zkMatch.global.utils.JsonUtil;
import com.zkrypto.zkMatch.global.utils.MultiBaseType;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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

    public static ApplyQrResponse from(UUID memberId) {
        String jsonString = JsonUtil.serializeAndSort(new RequestApplyDto(memberId.toString()));
        String encDataPayload = BaseMultibaseUtil.encode(jsonString.getBytes(), MultiBaseType.base64);
        return new ApplyQrResponse(encDataPayload, "APPLY", "");
    }

    @AllArgsConstructor
    public static class RequestApplyDto {
        private String memberId;
    }
}
