package com.zkrypto.zkMatch.domain.auth.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SignUpCommand {
    private String ci;
    private String loginId;
    private String password;
    private String email;
    private String emailAuthNumber;
    private String name;
    private String phoneNumber;
    private String birth;
    private String gender;
    private List<String> interests;
}
