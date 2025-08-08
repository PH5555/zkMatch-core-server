package com.zkrypto.zkMatch.domain.post.application.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCreationCommand {
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String majorRequirement;
    private String educationRequirement;
    private String experienceRequirement;
    private List<String> licenseRequirement;
    private int salaryStart;
    private int salaryEnd;
    private String workSpace;
    private String workType;
    private List<String> preferredSkill;
}
