package com.zkrypto.zkMatch.domain.corporation.application.dto.response;

import com.zkrypto.zkMatch.domain.evaluation.domain.entity.Evaluation;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import lombok.Getter;

@Getter
public class EvaluationResponse {
    private Long evaluationId;
    private String corporationName;
    private int rating;
    private String createdAt;
    private String evaluation;

    private EvaluationResponse(Long evaluationId, String corporationName, int rating, String createdAt, String evaluation) {
        this.evaluationId = evaluationId;
        this.corporationName = corporationName;
        this.rating = rating;
        this.createdAt = createdAt;
        this.evaluation = evaluation;
    }

    public static EvaluationResponse from(Evaluation evaluation) {
        return new EvaluationResponse(evaluation.getEvaluationId(), evaluation.getRecruit().getPost().getCorporation().getCorporationName(), evaluation.getRating(), DateFormatter.format(evaluation.getCreatedAt()), evaluation.getEvaluation());
    }
}
