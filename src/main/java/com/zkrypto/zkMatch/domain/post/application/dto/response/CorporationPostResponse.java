package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CorporationPostResponse {
    private String postId;
    private String title;
    private int applierCount;
    private String endDate;
    private String status;
    private String workType;
    private String experienceRequirement;

    public CorporationPostResponse(String postId, String title, int applierCount, String endDate, String status, String workType, String experienceRequirement) {
        this.postId = postId;
        this.title = title;
        this.applierCount = applierCount;
        this.endDate = endDate;
        this.status = status;
        this.workType = workType;
        this.experienceRequirement = experienceRequirement;
    }

    public static CorporationPostResponse from(Post post, int applierCount) {
        return new CorporationPostResponse(
                post.getPostId().toString(),
                post.getTitle(),
                applierCount,
                DateFormatter.format(post.getEndDate()),
                post.getEndDate().isAfter(LocalDateTime.now()) ? "마감" : "모집중",
                post.getWorkType(),
                post.getExperienceRequirement() > 0 ? post.getExperienceRequirement() + "년 이상" : "경력무관"
                );
    }
}
