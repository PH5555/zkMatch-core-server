package com.zkrypto.zkMatch.api.tas.constant;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VcPlan {
    private String vcPlanId;
    private String name;
    private String description;
    private String ref;
    private LogoImage logo;
    private String validFrom;
    private String validUntil;
    private List<String> tags;
    private CredentialSchema credentialSchema;
    private CredentialDefinition credentialDefinition;
    private Option option;
    private String delegator;
    private List<String> allowedIssuers;
    private String manager;
}