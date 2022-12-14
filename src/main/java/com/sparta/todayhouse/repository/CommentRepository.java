package com.sparta.todayhouse.repository;

import com.sparta.todayhouse.entity.Comment;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);

    List<Comment> findAllByPostOrderByCreatedAtDesc(Post post);
}
