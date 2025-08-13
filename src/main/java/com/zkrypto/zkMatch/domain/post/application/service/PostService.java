package com.zkrypto.zkMatch.domain.post.application.service;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.domain.recruit.domain.repository.RecruitRepository;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RecruitRepository recruitRepository;

    /**
     * 공고 조회 메서드
     */
    public List<PostResponse> getPost() {
        List<Post> posts = postRepository.findAllWithCorporation();
        return posts.stream().map(PostResponse::from).toList();
    }

    /**
     * 공고 지원 메서드
     */
    public void applyPost(UUID memberId, PostApplyCommand postApplyCommand) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 공고 확인
        Post post = postRepository.findById(UUID.fromString(postApplyCommand.getPostId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 이미 지원한 공고인지 확인
        if(recruitRepository.existsByMemberAndPost(member, post)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED_POST);
        }

        // 제출 기한이 지난 공고인지 확인
        if(LocalDateTime.now().isAfter(post.getEndDate())) {
            throw new CustomException(ErrorCode.EXPIRED_POST);
        }

        // 지원
        Recruit recruit = new Recruit(post, member);
        recruitRepository.save(recruit);
    }

    /**
     * 관심 공고 조회 메서드
     */
    public List<PostResponse> getInterestPost(UUID memberId) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 관심 분야 조회
        return member.getInterests().stream().flatMap(interest -> postRepository.findPostsByCategoryContaining(interest).stream())
                .map(PostResponse::from).toList();
    }
}
