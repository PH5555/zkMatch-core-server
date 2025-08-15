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

    private CorporationPostResponse(String postId, String title, int applierCount, String endDate) {
        this.postId = postId;
        this.title = title;
        this.applierCount = applierCount;
        this.endDate = endDate;
    }

    public static CorporationPostResponse from(Post post, int applierCount) {
        return new CorporationPostResponse(post.getPostId().toString(), post.getTitle(), applierCount, DateFormatter.format(post.getEndDate()));
    }
}
