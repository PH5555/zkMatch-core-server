package com.zkrypto.zkMatch.domain.interview.domain.repository;

import com.zkrypto.zkMatch.domain.interview.domain.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    @Query("select interview from Interview interview where interview.recruit.id = :id")
    Optional<Interview> findByRecruitId(@Param("id") Long recruitId);
}
