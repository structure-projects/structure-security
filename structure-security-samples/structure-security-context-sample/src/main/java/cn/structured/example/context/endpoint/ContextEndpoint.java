package cn.structured.example.context.endpoint;

import cn.structured.security.entity.StructureAuthUser;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.starter.context.manager.IContextManager;
import cn.structured.starter.context.store.IUserStore;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Context Sample Endpoint
 *
 * @author chuck
 * @since 1.0
 **/
@RestController
@RequestMapping("/api/context")
public class ContextEndpoint {

    @Autowired
    private IContextManager contextManager;

    @Autowired
    private IUserStore userStore;

    @GetMapping("/login")
    public String login(@RequestParam String userId, @RequestParam(required = false) String userName) {
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

        UserContextEntity user = UserContextEntity.builder()
                .userId(userId)
                .loginTime(LocalDateTime.now())
                .roles(Collections.emptySet())
                .permissions(Collections.emptySet())
                .build();
        contextManager.login(user);
        return "Login success: " + userId;
    }

    @GetMapping("/logout")
    public String logout() {
//        SecurityContextHolder.getContext().setAuthentication(null);
        contextManager.logout();
        return "Logout success";
    }

    @GetMapping("/current")
    public String getCurrentUser() {
        UserContextEntity user = contextManager.getUser();
        if (user != null) {
            return "Current user: " + user.getUserId();
        }
        return "No user logged in";
    }

    @GetMapping("/store/add")
    public String addUser(@RequestParam String userId) {
        UserContextEntity user = UserContextEntity.builder()
                .userId(userId)
                .loginTime(LocalDateTime.now())
                .roles(Collections.emptySet())
                .permissions(Collections.emptySet())
                .build();
        userStore.addUser(user);
        return "User added: " + userId;
    }

    @GetMapping("/store/get")
    public String getUser(@RequestParam String userId) {
        UserContextEntity user = userStore.getUser(userId);
        if (user != null) {
            return "User found: " + user.getUserId();
        }
        return "User not found: " + userId;
    }

    @GetMapping("/store/remove")
    public String removeUser(@RequestParam String userId) {
        userStore.removeUser(userId);
        return "User removed: " + userId;
    }
}