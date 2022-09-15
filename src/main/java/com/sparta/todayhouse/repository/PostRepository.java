package com.sparta.todayhouse.repository;

import com.sparta.todayhouse.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
//    Optional<Post> findFirstByOrderBy

    List<Post> findFirst6ByIdGreaterThan(Long id);

    Page<Post> findAll(Pageable pageable);
}
