package com.zkrypto.zkMatch.domain.corporation.application.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostUpdateCommand {
    private String postId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String majorRequirement;
    private String educationRequirement;
    private int experienceRequirement;
    private List<String> licenseRequirement;
    private int salaryStart;
    private int salaryEnd;
    private String workSpace;
    private String workType;
    private List<String> preferredSkill;
    private List<String> category;
}
