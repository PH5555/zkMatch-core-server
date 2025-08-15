package com.zkrypto.zkMatch.domain.evaluation.domain.entity;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.EvaluationCreationCommand;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Evaluation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    private String evaluation;
    private int rating;
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    private Evaluation(String evaluation, int rating, LocalDateTime createdAt, Recruit recruit) {
        this.evaluation = evaluation;
        this.rating = rating;
        this.createdAt = createdAt;
        this.recruit = recruit;
    }

    public static Evaluation from(EvaluationCreationCommand command, Recruit recruit) {
        return new Evaluation(command.getEvaluation(), command.getRating(), LocalDateTime.now(), recruit);
    }
}
