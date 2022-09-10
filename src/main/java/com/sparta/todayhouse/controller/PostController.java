package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.service.PostService;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Post createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        Member member = userDetails.getMember();
//        if(null == member) return error;
        return postService.createPost(requestDto);
    }

    @RequestMapping(value = "{post_id}", method = RequestMethod.GET)
    public Post getPost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.getPost(post_id);
    }

    @RequestMapping(value = "{post_id}", method = RequestMethod.PUT)
    public Post updatePost(@PathVariable Long post_id, @RequestBody PostRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.updatePost(post_id, requestDto);
    }

    @RequestMapping(value = "{post_id}", method = RequestMethod.DELETE)
    public Long deletePost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deletePost(post_id);
    }
}
