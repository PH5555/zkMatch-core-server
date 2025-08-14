package com.zkrypto.zkMatch.api.verify.dto.response;

import com.zkrypto.zkMatch.api.verify.constant.VerifyOfferPayload;
import lombok.Getter;

@Getter
public class RequestVpOfferResDto {
    private String txId;
    private VerifyOfferPayload payload;
    private byte[] qrImage;
    private String payloadType;
}
