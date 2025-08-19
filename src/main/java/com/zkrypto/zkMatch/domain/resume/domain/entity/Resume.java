package com.zkrypto.zkMatch.domain.resume.domain.entity;

import com.zkrypto.zkMatch.domain.member.application.dto.request.ResumeCreationCommand;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Resume {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumeId;

    @Enumerated(EnumType.STRING)
    private ResumeType resumeType;

    @Column(length = 1000)
    private String encData;
    private Boolean did;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Resume(ResumeType resumeType, String encData, Boolean did, Member member) {
        this.resumeType = resumeType;
        this.encData = encData;
        this.did = did;
        this.member = member;
    }

    public static Resume from(ResumeCreationCommand command, String data, Member member) {
        return new Resume(command.getResumeType(), data, false, member);
    }

    public static Resume from(ResumeType type, String data, Member member) {
        return new Resume(type, data, true, member);
    }
}
