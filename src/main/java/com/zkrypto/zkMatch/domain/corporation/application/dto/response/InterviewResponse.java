package com.zkrypto.zkMatch.domain.corporation.application.dto.response;

import com.zkrypto.zkMatch.domain.interview.domain.entity.Interview;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import lombok.Getter;

@Getter
public class InterviewResponse {
    private String interviewId;
    private String interviewName;
    private String date;
    private String location;
    private String interviewer;

    public InterviewResponse(String id, String interviewName, String date, String location, String interviewer) {
        this.interviewId = id;
        this.interviewName = interviewName;
        this.date = date;
        this.location = location;
        this.interviewer = interviewer;
    }

    public static InterviewResponse from(Interview interview) {
        String startAt = DateFormatter.format(interview.getStartAt());
        String endAt = DateFormatter.format(interview.getEndAt());
        return new InterviewResponse(interview.getInterviewId().toString(), interview.getTitle(), startAt + " ~ " + endAt , interview.getLocation(), interview.getInterviewer());
    }
}
