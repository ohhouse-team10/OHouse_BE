package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final TestService testService;

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public Post createPost(PostRequestDto requestDto) {
        Member member = testService.isPresentMember("admin@gmail.com");

        Post post = postRepository.save(Post.builder()
                .content(requestDto.getContent())
                .thumbnail("")
                .member(member)
                .build());
        return post;
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return isPresentPost(id);
    }

    @Transactional
    public Post updatePost(Long id, PostRequestDto requestDto) {
        Post post = isPresentPost(id);
        if (null == post) return post;

        post.update(requestDto);

        return post;
    }

    @Transactional
    public Long deletePost(Long id) {
        Post post = isPresentPost(id);
        if (null == post) return null;

        postRepository.delete(post);

        return id;
    }
}
