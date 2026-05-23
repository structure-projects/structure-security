package cn.structure.example.jwt.endpoint;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {
    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/hello2")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String hello2() {
        return "hello world";
    }

}
