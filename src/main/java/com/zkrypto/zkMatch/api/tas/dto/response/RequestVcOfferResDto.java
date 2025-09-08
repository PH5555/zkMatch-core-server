package com.zkrypto.zkMatch.api.tas.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestVcOfferResDto {
    private String offerId;
    private String validUntil;
    private IssuerOfferPayload issueOfferPayload;
    private byte[] qrImage;
    private boolean result;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class IssuerOfferPayload {
        private String offerId;
        private String vcPlanId;
        private String type;
        private String issuer;
        private String validUntil;
    }
}