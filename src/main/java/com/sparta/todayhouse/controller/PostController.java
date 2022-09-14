package com.sparta.todayhouse.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.service.AwsS3Service;
import com.sparta.todayhouse.service.PostService;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @RequestMapping(value = "/auth/post", method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@RequestPart(value = "data") PostRequestDto requestDto,
                                        @RequestPart(value = "file") MultipartFile multipartFile,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = postService.createPost(requestDto, multipartFile, userDetails);
        return ResponseEntity.ok().body(data);
    }

//    @RequestMapping(value = "/post/all", method = RequestMethod.GET)
//    public ResponseEntity<?> getAllPost(){
//        ResponseMessage<?> data = postService.getAllPost();
//        return ResponseEntity.ok().body(data);
//    }


    @RequestMapping(value = "/post", method = RequestMethod.GET)                     //이중맵핑, 클래스매핑
    public ResponseEntity<?> getPostPerPage(Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = (null == userDetails) ? postService.getPostPerPage(pageable) :
                postService.getPostPerPage(pageable, userDetails);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/post/{post_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = postService.isPresentPost(post_id);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/auth/post/{post_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePost(@PathVariable Long post_id,
                                        @RequestPart(value = "data") PostRequestDto requestDto,
                                        @RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseMessage<?> data = postService.updatePost(post_id, requestDto, multipartFile, userDetails);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/auth/post/{post_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long post_id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = postService.deletePost(post_id, userDetails);
        return ResponseEntity.ok().body(data);
    }
}
