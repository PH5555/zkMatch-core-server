package com.zkrypto.zkMatch.domain.resume.domain.repository;

import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;

import java.util.List;

public interface ResumeCustomRepository {
    List<Resume> findCandidateResume(List<String> licenses, int employPeriod, String educationType);
}
