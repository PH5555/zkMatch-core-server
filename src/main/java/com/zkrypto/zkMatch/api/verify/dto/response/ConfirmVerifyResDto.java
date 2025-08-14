package com.zkrypto.zkMatch.api.verify.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ConfirmVerifyResDto {
    private String vc;
    private String issuer;
    private List<Claim> claims;
    private Boolean result;
    @Getter
    @Setter
    public static class Claim {
        private String caption;
        private String code;
        private String format;
        private boolean hideValue;
        private String type;
        private String value;
    }
}