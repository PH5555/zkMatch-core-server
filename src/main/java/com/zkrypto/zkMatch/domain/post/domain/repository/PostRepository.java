package com.zkrypto.zkMatch.domain.post.domain.repository;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID>, PostCustomRepository {
    List<Post> findPostByCorporation(Corporation corporation);

    @Query("select post from Post post left join fetch post.corporation")
    List<Post> findAllWithCorporation();

    @Query("select post from Post post left join fetch post.corporation where post.postId = :postId")
    Optional<Post> findByIdWithCorporation(@Param("postId") UUID postId);

    List<Post> findPostByCorporationAndTitleContaining(Corporation corporation, String title);


    @Query(value = "SELECT * FROM post WHERE category LIKE %:keyword%", nativeQuery = true)
    List<Post> findByCategory(@Param("keyword") String keyword);

    Optional<Post> findPostByTitle(String title);

    Optional<Post> getPostByTitle(String title);
}
