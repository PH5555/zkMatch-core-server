package com.zkrypto.zkMatch.domain.post.application.dto.request;

import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class PostCreationCommand {
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
    private List<String> category;
    private PostType postType;
}
