package cn.structure.starter.oauth.resource.example.controller;

import cn.structured.security.context.UserContext;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/hello")
    public String hello() {
        Long userId =  SecurityUtils.getUserId();
        log.info("userId: {}", userId);

        UserContextEntity userContextEntity = UserContext.get();
        log.info("userContextEntity: {}", userContextEntity);
        return "hello world";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/hello2")
    public String hello2() {
        return "hello world2";
    }
}
