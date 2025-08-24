package com.zkrypto.zkMatch.domain.application.domain.entity;

import com.zkrypto.zkMatch.domain.application.domain.constant.Status;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private LocalDateTime validTime;
    private LocalDateTime createdAt;

    public Application(Member member, Post post) {
        this.member = member;
        this.post = post;
        this.status = Status.PENDING;
        this.validTime = LocalDateTime.now().plusMinutes(5);
        this.createdAt = LocalDateTime.now();
    }

    public void success() {
        this.status = Status.SUCCESS;
    }
}
