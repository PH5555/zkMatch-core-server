package com.zkrypto.zkMatch.domain.post.application.dto.response;

import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String endDate = post.getEndDate().format(formatter);
        return new CorporationPostResponse(post.getPostId().toString(), post.getTitle(), applierCount, endDate);
    }
}
