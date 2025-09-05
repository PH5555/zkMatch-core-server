package com.zkrypto.zkMatch.domain.recruit.domain.repository;

import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;

import java.util.List;

public interface RecruitCustomRepository {
    List<Recruit> findByMemberAndType(Member member, PostType postType);
}
