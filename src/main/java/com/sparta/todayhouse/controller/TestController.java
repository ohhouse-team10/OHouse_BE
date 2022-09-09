package com.sparta.todayhouse.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String testGet() { return "success_get"; }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String testPost() { return "success_post"; }

    @RequestMapping(value = "/put", method = RequestMethod.PUT)
    public String testPut() { return "success_put"; }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String testDelete() { return "success_delete"; }
}
