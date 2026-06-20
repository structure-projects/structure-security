package cn.structured.example.core.endpoint;

import cn.structured.security.context.UserContext;
import cn.structured.security.entity.StructureAuthUser;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.util.SecurityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core Sample Endpoint
 *
 * @author chuck
 * @since 1.0
 **/
@RestController
@RequestMapping("/api/core")
public class CoreEndpoint {

    @GetMapping("/set-user")
    public String setUser(@RequestParam String userId) {
        StructureAuthUser authUser = new StructureAuthUser();
        authUser.setId("1");
        authUser.setUsername("admin");
        authUser.setPassword("123456");
        authUser.setEnable(true);
        authUser.setUnlocked(true);
        authUser.setUnexpired(true);
        authUser.setCreateTime(LocalDateTime.now());
        authUser.setUpdateTime(LocalDateTime.now());
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 超级管理员：拥有所有权限
        authorities.add(new SimpleGrantedAuthority("*:*"));           // 所有二层权限
        authorities.add(new SimpleGrantedAuthority("*:*:*"));         // 所有三层权限
        authUser.setAuthorities(authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

        // 认证成功，下个过滤器就会放行了。
        // 每个请求有效，下个请求authentication就变了
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserContextEntity context = UserContextEntity.builder()
                .userId(userId)
                .loginTime(LocalDateTime.now())
                .roles(Collections.emptySet())
                .permissions(Collections.emptySet())
                .build();
        UserContext.set(context);
        return "User set: " + userId;
    }

    @GetMapping("/get-user")
    public String getUser() {
        UserContextEntity context = UserContext.get();
        if (context != null) {
            return "Current user: " + context.getUserId();
        }
        return "No user context";
    }

    @GetMapping("/get-user-id")
    public String getUserId() {
        Long userId = SecurityUtils.getUserId();
        return "User ID: " + userId;
    }

    @GetMapping("/clear")
    public String clear() {
        UserContext.remove();
        return "User context cleared";
    }

    @GetMapping("/check-authenticated")
    public String checkAuthenticated() {
        boolean isAuthenticated = SecurityUtils.getAuthentication() != null;
        return "Is authenticated: " + isAuthenticated;
    }
}