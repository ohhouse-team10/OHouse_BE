package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.LoginRequestDto;
import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.dto.response.MemberResponseDto;
import com.sparta.todayhouse.dto.response.ResponseDto;
import com.sparta.todayhouse.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value="/member/signup",method= RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto requestDto){
        ResponseMessage<?> data = memberService.signup(requestDto);
        return ResponseEntity.ok().body(data);
    }
//
    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        ResponseMessage<?> data = memberService.login(requestDto, response);
        return ResponseEntity.ok().body(data);
    }
//
//    @RequestMapping(value ="/auth/member/logout", method = RequestMethod.GET)
//    public ResponseDto<?> logout(HttpServletRequest request){
//        return memberService.logout(request);
//    }



}
