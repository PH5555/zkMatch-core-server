package com.zkrypto.zkMatch.domain.corporation.application.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InterviewUpdateCommand {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String interviewer;
}
