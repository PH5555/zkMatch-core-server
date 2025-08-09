package com.zkrypto.zkMatch.domain.corporation.application.dto.request;

import lombok.Getter;

@Getter
public class EvaluationCreationCommand {
    private Long recruitId;
    private String evaluation;
}
