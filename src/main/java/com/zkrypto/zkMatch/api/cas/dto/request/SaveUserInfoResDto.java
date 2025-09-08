package com.zkrypto.zkMatch.api.cas.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SaveUserInfoResDto {
    private String userId;
    private String pii;
    private boolean result;
}
