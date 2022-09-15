package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.service.LikesService;
import com.sparta.todayhouse.shared.ErrorHandler;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @RequestMapping(value = "/auth/likes/{post_id}", method = RequestMethod.POST)
    public ResponseEntity<?> createLikes(@PathVariable Long post_id,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = likesService.createLikes(post_id, userDetails);
        return ErrorHandler.returnResponse(data);
    }

    @RequestMapping(value = "/auth/likes/{post_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteLikes(@PathVariable Long post_id,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = likesService.deleteLikes(post_id, userDetails);
        return ErrorHandler.returnResponse(data);
    }
}
