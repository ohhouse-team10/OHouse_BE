package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.request.LoginRequestDto;
import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.dto.response.MemberResponseDto;
import com.sparta.todayhouse.dto.response.ResponseDto;
import com.sparta.todayhouse.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

//    @RequestMapping(value="/member/signup",method= RequestMethod.POST)
    @PostMapping("/member/signup")
    public ResponseDto<MemberResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto){
        return memberService.signup(requestDto);
    }
//
    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        return memberService.login(requestDto, response);
    }
//
//    @RequestMapping(value ="/auth/member/logout", method = RequestMethod.GET)
//    public ResponseDto<?> logout(HttpServletRequest request){
//        return memberService.logout(request);
//    }



}
