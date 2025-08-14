package com.zkrypto.zkMatch.api.verify.constant;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VerifyOfferPayload {
    private String offerId;
    private OfferType type;
    private PresentMode mode;
    private String device;
    private String service;
    private ArrayList<String> endpoints;
    private String validUntil;
    private Boolean locked;
}