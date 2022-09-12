package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.service.PostService;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        Member member = userDetails.getMember();
//        if(null == member) return error;
        ResponseMessage<?> response = postService.createPost(requestDto);
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(value = "{post_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> response = postService.getPost(post_id);
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(value = "{post_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePost(@PathVariable Long post_id, @RequestBody PostRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> response = postService.updatePost(post_id, requestDto);
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(value = "{post_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> response = postService.deletePost(post_id);
        return ResponseEntity.ok().body(response);
    }
}
