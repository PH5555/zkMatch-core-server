package com.zkrypto.zkMatch.api.tas.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestVcOfferReqDto {
    private String id;
    private String vcPlanId;
    private String issuer;
    private String holder;
    private String email;
    private String did;
}