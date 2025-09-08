package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.scrab.domain.entity.Scrab;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class PostResponse {
    private String postId;
    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String majorRequirement;
    private String educationRequirement;
    private String experienceRequirement;
    private List<String> licenseRequirement;
    private int salaryStart;
    private int salaryEnd;
    private String workSpace;
    private String workType;
    private List<String> category;
    private String corporationName;
    private PostType postType;

    public PostResponse(String postId, String title, String content, String startDate, String endDate, String majorRequirement, String educationRequirement, String experienceRequirement, List<String> licenseRequirement, int salaryStart, int salaryEnd, String workSpace, String workType, List<String> category, String corporationName, PostType postType) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.majorRequirement = majorRequirement;
        this.educationRequirement = educationRequirement;
        this.experienceRequirement = experienceRequirement;
        this.licenseRequirement = licenseRequirement;
        this.salaryStart = salaryStart;
        this.salaryEnd = salaryEnd;
        this.workSpace = workSpace;
        this.workType = workType;
        this.category = category;
        this.corporationName = corporationName;
        this.postType = postType;
    }

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId().toString(),
                post.getTitle(),
                post.getContent(),
                DateFormatter.format(post.getStartDate()),
                DateFormatter.format(post.getEndDate()),
                post.getMajorRequirement(),
                post.getEducationRequirement(),
                post.getExperienceRequirement() + "년 이상",
                post.getLicenseRequirement(),
                post.getSalaryStart(),
                post.getSalaryEnd(),
                post.getWorkSpace(),
                post.getWorkType(),
                post.getCategory(),
                post.getCorporation().getCorporationName(),
                post.getPostType()
        );
    }

    public static PostResponse from(Scrab scrab) {
        Post post = scrab.getPost();

        return new PostResponse(
                post.getPostId().toString(),
                post.getTitle(),
                post.getContent(),
                DateFormatter.format(post.getStartDate()),
                DateFormatter.format(post.getEndDate()),
                post.getMajorRequirement(),
                post.getEducationRequirement(),
                post.getExperienceRequirement() + "년 이상",
                post.getLicenseRequirement(),
                post.getSalaryStart(),
                post.getSalaryEnd(),
                post.getWorkSpace(),
                post.getWorkType(),
                post.getCategory(),
                post.getCorporation().getCorporationName(),
                post.getPostType()
        );
    }
}
