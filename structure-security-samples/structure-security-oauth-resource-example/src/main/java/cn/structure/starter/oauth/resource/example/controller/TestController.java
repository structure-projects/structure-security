package cn.structure.starter.oauth.resource.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/hello2")
    public String hello2() {
        return "hello world2";
    }
}
