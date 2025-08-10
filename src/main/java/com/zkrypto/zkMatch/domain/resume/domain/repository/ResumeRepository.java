package com.zkrypto.zkMatch.domain.resume.domain.repository;

import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
