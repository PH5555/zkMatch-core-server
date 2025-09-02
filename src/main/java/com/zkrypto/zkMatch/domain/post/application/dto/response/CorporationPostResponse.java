package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class CorporationPostResponse {
    private String postId;
    private String title;
    private int applierCount;
    private String endDate;
    private String status;
    private String workType;
    private String experienceRequirement;
    private String content;
    private String startDate;
    private String majorRequirement;
    private String educationRequirement;
    private List<String> licenseRequirement;
    private int salaryStart;
    private int salaryEnd;
    private String workSpace;
    private List<String> category;

    public CorporationPostResponse(String postId, String title, int applierCount, String endDate, String status, String workType, String experienceRequirement, String content, String startDate, String majorRequirement, String educationRequirement, List<String> licenseRequirement, int salaryStart, int salaryEnd, String workSpace, List<String> category) {
        this.postId = postId;
        this.title = title;
        this.applierCount = applierCount;
        this.endDate = endDate;
        this.status = status;
        this.workType = workType;
        this.experienceRequirement = experienceRequirement;
        this.content = content;
        this.startDate = startDate;
        this.majorRequirement = majorRequirement;
        this.educationRequirement = educationRequirement;
        this.licenseRequirement = licenseRequirement;
        this.salaryStart = salaryStart;
        this.salaryEnd = salaryEnd;
        this.workSpace = workSpace;
        this.category = category;
    }

    public static CorporationPostResponse from(Post post, int applierCount) {
        return new CorporationPostResponse(
                post.getPostId().toString(),
                post.getTitle(),
                applierCount,
                DateFormatter.format(post.getEndDate()),
                post.getEndDate().isAfter(LocalDateTime.now()) ? "모집중" : "마감",
                post.getWorkType(),
                post.getExperienceRequirement() > 0 ? post.getExperienceRequirement() + "년 이상" : "경력무관",
                post.getContent(),
                DateFormatter.format(post.getStartDate()),
                post.getMajorRequirement(),
                post.getEducationRequirement(),
                post.getLicenseRequirement(),
                post.getSalaryStart(),
                post.getSalaryEnd(),
                post.getWorkSpace(),
                post.getCategory()
                );
    }
}
