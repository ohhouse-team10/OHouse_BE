package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.todayhouse.shared.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final TestService testService;

    @Transactional(readOnly = true)
    public ResponseMessage<?> isPresentPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if(null == post) return ResponseMessage.fail(POST_NOT_FOUND);

        return ResponseMessage.success(post);
    }

    @Transactional
    public ResponseMessage<?> createPost(PostRequestDto requestDto) {
        Member member = testService.isPresentMember("admin@gmail.com");

        Post post = postRepository.save(Post.builder()
                .content(requestDto.getContent())
                .thumbnail("")
                .member(member)
                .build());
        return ResponseMessage.success(post);
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getPost(Long id) {
        return isPresentPost(id);
    }

    @Transactional
    public ResponseMessage<?> updatePost(Long id, PostRequestDto requestDto) {
        ResponseMessage<?> response = isPresentPost(id);
        Post post = (Post)response.getData();
        if (null == post) return response;

        post.update(requestDto);

        return ResponseMessage.success(post);
    }

    @Transactional
    public ResponseMessage<?> deletePost(Long id) {
        ResponseMessage<?> response = isPresentPost(id);
        Post post = (Post)response.getData();
        if (null == post) return response;

        postRepository.delete(post);

        return ResponseMessage.success("success");
    }
}
