package com.zkrypto.zkMatch.domain.resume.domain.entity;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import jakarta.persistence.*;

@Entity
public class Resume {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumeId;
    private ResumeType resumeType;
    private String enc_data;
    private String did;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
