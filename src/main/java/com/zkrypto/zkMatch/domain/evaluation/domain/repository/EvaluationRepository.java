package com.zkrypto.zkMatch.domain.evaluation.domain.repository;

import com.zkrypto.zkMatch.domain.evaluation.domain.entity.Evaluation;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    @Query("select evaluation from Evaluation evaluation where evaluation.recruit.member = :member")
    public List<Evaluation> findEvaluationsByMember(@Param("member") Member member);
}
