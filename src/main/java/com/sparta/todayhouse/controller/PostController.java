package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.service.PostService;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @RequestMapping(value = "/auth/post", method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        Member member = userDetails.getMember();
//        if(null == member) return error;
        ResponseMessage<?> data = postService.createPost(requestDto, userDetails);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/post/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAllPost(){
        ResponseMessage<?> data = postService.getAllPost();
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public ResponseEntity<?> getPostPerPage(Pageable pageable){
        ResponseMessage<?> data = postService.getPostPerPage(pageable);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/post/{post_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = postService.isPresentPost(post_id);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/auth/post/{post_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePost(@PathVariable Long post_id, @RequestBody PostRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = postService.updatePost(post_id, requestDto);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/auth/post/{post_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = postService.deletePost(post_id);
        return ResponseEntity.ok().body(data);
    }
}
