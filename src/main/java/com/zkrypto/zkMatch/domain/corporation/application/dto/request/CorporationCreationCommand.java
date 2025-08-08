package com.zkrypto.zkMatch.domain.corporation.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CorporationCreationCommand {
    private String corporationType;
    private String corporationName;
    private String corporationAddress;
    private String corporationUrl;
    private String industryType;
    private String name;
    private String email;
    private String phoneNumber;
    private String loginId;
    private String password;
}
