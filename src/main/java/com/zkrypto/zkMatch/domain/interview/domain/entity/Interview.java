package com.zkrypto.zkMatch.domain.interview.domain.entity;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.InterviewCreationCommand;
import com.zkrypto.zkMatch.domain.corporation.application.dto.request.InterviewUpdateCommand;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Interview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String location;
    private String interviewer;

    @OneToOne(mappedBy = "interview")
    private Recruit recruit;

    private Interview(String title, LocalDateTime startAt, LocalDateTime endAt, String location, String interviewer) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.location = location;
        this.interviewer = interviewer;
    }

    public static Interview from(InterviewCreationCommand command) {
        return new Interview(command.getTitle(), command.getStartDate(), command.getEndDate(), command.getLocation(), command.getInterviewer());
    }

    public void update(InterviewUpdateCommand command) {
        this.title = command.getTitle();
        this.startAt = command.getStartDate();
        this.endAt = command.getEndDate();
        this.location = command.getLocation();
        this.interviewer = command.getInterviewer();
    }
}
