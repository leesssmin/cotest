package com.example.sm.repository;

import com.example.sm.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 제목으로 검색
    Page<Post> findByTitleContaining(String query, Pageable pageable);

    // 내용으로 검색
    Page<Post> findByContentContaining(String query, Pageable pageable);

    // 제목 또는 내용으로 검색
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> searchByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    // 전체 조회
    Page<Post> findAll(Pageable pageable);


}
