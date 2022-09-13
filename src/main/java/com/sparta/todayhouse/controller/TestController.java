package com.sparta.todayhouse.controller;

import com.sparta.todayhouse.dto.request.SignupRequestDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String testGet() { return "success_get!"; }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String testPost() { return "success_post!"; }

    @RequestMapping(value = "/put", method = RequestMethod.PUT)
    public String testPut() { return "success_put!"; }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String testDelete() { return "success_delete!"; }

    @RequestMapping(value = "/customtest", method = RequestMethod.POST)
    public Member customTest(@RequestBody SignupRequestDto requestDto){
        return testService.createAdmin(requestDto);
    }
}
