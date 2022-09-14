package com.sparta.todayhouse.repository;

import com.sparta.todayhouse.entity.Likes;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByMemberAndPost(Member member, Post post);

    List<Likes> findAllByMember(Member member);
    List<Likes> findAllByPost(Post post);
}
