package com.zkrypto.zkMatch.domain.resume.domain.entity;

import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class AppliedResume {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appliedResumeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    public AppliedResume(Recruit recruit, Resume resume) {
        this.recruit = recruit;
        this.resume = resume;
    }
}
