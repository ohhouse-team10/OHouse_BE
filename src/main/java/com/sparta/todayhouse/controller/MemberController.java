package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.LoginRequestDto;
import com.sparta.todayhouse.dto.request.MemberRequestDto;
import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.service.MemberService;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        ResponseMessage<?> data = memberService.login(requestDto, response);
        return ResponseEntity.ok().body(data);
    }

    @RequestMapping(value ="/auth/member/logout", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseMessage<?> data = memberService.logout(userDetails);

        return ResponseEntity.ok().body(data);
    }


    //회원정보수정
    @RequestMapping(value ="/auth/member/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMember(@RequestBody MemberRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseMessage<?> data = memberService.updateMember(requestDto, userDetails);
        return ResponseEntity.ok().body(data);

    }
}


    //회원정보요청



