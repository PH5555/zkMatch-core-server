package com.zkrypto.zkMatch.api.tas.constant;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LogoImage {
    private LogoImageType format;
    private String link;
    private String value;
}
