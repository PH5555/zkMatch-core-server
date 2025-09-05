package com.zkrypto.zkMatch.domain.post.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.post.domain.constant.PostType;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zkrypto.zkMatch.domain.post.domain.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostWithKeyword(Corporation corporation, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.corporation.eq(corporation));
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(post.title.contains(keyword));
        }
        return jpaQueryFactory.selectFrom(post)
                .where(builder).fetch();
    }

    @Override
    public List<Post> findPostWithType(PostType postType) {
        BooleanBuilder builder = new BooleanBuilder();

        if(postType != null && !postType.toString().isEmpty()) {
            builder.and(post.postType.eq(postType));
        }
        return jpaQueryFactory.selectFrom(post).where(builder).fetch();
    }
}
