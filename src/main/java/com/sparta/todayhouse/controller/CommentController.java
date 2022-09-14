package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.CommentRequestDto;
import com.sparta.todayhouse.entity.Comment;
import com.sparta.todayhouse.service.CommentService;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @RequestMapping(value = "/auth/comment/{post_id}", method = RequestMethod.POST)
    public ResponseEntity<?> createComment(@PathVariable Long post_id, @RequestBody CommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = commentService.createComment(post_id, requestDto, userDetails);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/comment/{post_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCommentByPost(@PathVariable Long post_id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = commentService.getCommentByPost(post_id);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/auth/comment/{comment_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateComment(@PathVariable Long comment_id, @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = commentService.updateComment(comment_id, requestDto, userDetails);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value = "/auth/comment/{comment_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> createComment(@PathVariable Long comment_id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = commentService.deleteComment(comment_id, userDetails);
        return ResponseEntity.ok().body(data);
    }
}
