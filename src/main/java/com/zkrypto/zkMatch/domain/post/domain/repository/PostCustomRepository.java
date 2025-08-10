package com.zkrypto.zkMatch.domain.post.domain.repository;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;

import java.util.List;

public interface PostCustomRepository {
    List<Post> findPostWithKeyword(Corporation corporation, String keyword);
}
