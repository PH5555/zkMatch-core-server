package com.zkrypto.zkMatch.api.tas.constant;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Option {
    private Boolean allowUserInit;
    private Boolean allowIssuerInit;
    private Boolean delegatedIssuance;
}