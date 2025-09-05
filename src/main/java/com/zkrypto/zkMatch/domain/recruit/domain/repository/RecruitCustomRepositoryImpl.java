package com.zkrypto.zkMatch.domain.recruit.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.zkrypto.zkMatch.domain.recruit.domain.entity.QRecruit.recruit;

@RequiredArgsConstructor
public class RecruitCustomRepositoryImpl implements RecruitCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Recruit> findByMemberAndType(Member member, PostType postType) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(recruit.member.eq(member));

        if(postType != null && !postType.toString().isEmpty()) {
            builder.and(recruit.post.postType.eq(postType));
        }

        return jpaQueryFactory.selectFrom(recruit)
                .where(builder)
                .fetch();
    }
}
