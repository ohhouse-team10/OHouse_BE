package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.service.PostService;
import com.sparta.todayhouse.error.ErrorHandler;
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
        return ErrorHandler.returnResponse(data);
    }

    @RequestMapping(value = "/post/main", method = RequestMethod.GET)
    public ResponseEntity<?> getPostMain(){
        ResponseMessage<?> data = postService.getPostMain();
        return ErrorHandler.returnResponse(data);
    }


    @RequestMapping(value = "/post", method = RequestMethod.GET)                     //이중맵핑, 클래스매핑
    public ResponseEntity<?> getPostPerPage(Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = (null == userDetails) ? postService.getPostPerPage(pageable) :
                postService.getPostPerPage(pageable, userDetails);
        return ErrorHandler.returnResponse(data);
    }

    @RequestMapping(value = "/post/{post_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPostDetail(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = (null == userDetails) ? postService.getPostDetail(post_id) :
                postService.getPostDetail(post_id, userDetails);
        return ErrorHandler.returnResponse(data);
    }

    @RequestMapping(value = "/auth/post/{post_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePost(@PathVariable Long post_id,
                                        @RequestPart(value = "data") PostRequestDto requestDto,
                                        @RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseMessage<?> data = postService.updatePost(post_id, requestDto, multipartFile, userDetails);
        return ErrorHandler.returnResponse(data);
    }

    @RequestMapping(value = "/auth/post/{post_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long post_id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = postService.deletePost(post_id, userDetails);
        return ErrorHandler.returnResponse(data);
    }
}
