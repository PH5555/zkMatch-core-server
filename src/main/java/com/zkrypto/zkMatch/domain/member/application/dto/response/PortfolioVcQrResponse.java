package com.zkrypto.zkMatch.domain.member.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PortfolioVcQrResponse {
    private String payload;
    private String payloadType;
    private byte[] qrImage;
    private String validUntil;
    private String offerId;
}