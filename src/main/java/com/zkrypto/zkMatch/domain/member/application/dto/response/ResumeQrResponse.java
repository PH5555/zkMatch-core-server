package com.zkrypto.zkMatch.domain.member.application.dto.response;

import com.zkrypto.zkMatch.api.verify.dto.response.RequestVpOfferResDto;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResumeQrResponse {
    private String txId;
    private String payload;
    private String payloadType;
    private String validUntil;

    @Setter
    private String offerId;

    @Setter
    private byte[] qrImage;

    public ResumeQrResponse(String payload, String payloadType, String validUntil) {
        this.payload = payload;
        this.payloadType = payloadType;
        this.validUntil = validUntil;
    }

    public static ResumeQrResponse from(String encDataPayload, RequestVpOfferResDto requestVpOfferResDto) {
        return new ResumeQrResponse("SUBMIT_VP", encDataPayload, requestVpOfferResDto.getPayload().getValidUntil());
    }
}
