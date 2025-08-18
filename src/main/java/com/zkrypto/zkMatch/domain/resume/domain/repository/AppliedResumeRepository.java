package com.zkrypto.zkMatch.domain.resume.domain.repository;

import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.resume.domain.entity.AppliedResume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppliedResumeRepository extends JpaRepository<AppliedResume, Long> {
    List<AppliedResume> findAppliedResumesByRecruit(Recruit recruit);
}
