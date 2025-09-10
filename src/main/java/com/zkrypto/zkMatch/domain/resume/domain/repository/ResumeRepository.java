package com.zkrypto.zkMatch.domain.resume.domain.repository;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> getResumesByMember(Member member);

    Optional<Resume> getResumeByResumeId(Long resumeId);

    List<Resume> getResumesByMemberAndResumeType(Member member, ResumeType resumeType);

    long countByMemberAndResumeType(Member member, ResumeType resumeType);
}