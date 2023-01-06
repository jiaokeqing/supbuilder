package com.supbuilder.user.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "用户管理模块")
//@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class TestController {

    @GetMapping("test")
    public String test(){
        return "测试";
    }
}
