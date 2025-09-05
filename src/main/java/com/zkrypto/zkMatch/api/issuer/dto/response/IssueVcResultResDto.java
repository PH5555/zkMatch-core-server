package com.zkrypto.zkMatch.api.issuer.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IssueVcResultResDto {
    boolean result;
    String txId;
}
