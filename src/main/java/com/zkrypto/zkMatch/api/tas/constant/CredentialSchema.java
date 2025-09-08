package com.zkrypto.zkMatch.api.tas.constant;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CredentialSchema {
    private String id;
    private CredentialSchemaType type;
    private String value;
}