package com.zkrypto.zkMatch.domain.resume.domain.repository;

import com.zkrypto.zkMatch.domain.resume.domain.entity.AppliedResume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedResumeRepository extends JpaRepository<AppliedResume, Long> {
}
