package com.zkrypto.zkMatch.domain.scrab.application.service;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.member.domain.repository.MemberRepository;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.post.domain.repository.PostRepository;
import com.zkrypto.zkMatch.domain.scrab.application.dto.request.ScrabCommand;
import com.zkrypto.zkMatch.domain.scrab.application.dto.response.ScrabResponse;
import com.zkrypto.zkMatch.domain.scrab.domain.entity.Scrab;
import com.zkrypto.zkMatch.domain.scrab.domain.repository.ScrabRepository;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScrabService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ScrabRepository scrabRepository;

    /**
     * 공고 스크랩 메서드
     */
    public void scrabPost(UUID memberId, ScrabCommand scrabCommand) {
        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 공고 확인
        Post post = postRepository.findById(UUID.fromString(scrabCommand.getPostId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 스크랩 여부 조회
        Optional<Scrab> scrab = scrabRepository.findByMemberAndPost(member, post);

        // 이미 스크랩 되어 있으면 삭제, 스크랩 이력이 없으면 스크랩 데이터 저장
        scrab.ifPresentOrElse(
                scrabRepository::delete,
                () -> scrabRepository.save(new Scrab(member, post))
        );
    }
}
