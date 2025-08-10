package com.zkrypto.zkMatch.domain.recruit.domain.entity;

import com.zkrypto.zkMatch.domain.interview.domain.entity.Interview;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Recruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @Setter
    private String evaluation;

    public Recruit(Post post, Member member) {
        this.post = post;
        this.member = member;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
        this.status = Status.INTERVIEW;
    }
}
