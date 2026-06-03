package cn.structured.security.basicauth.server.sample.service;

import cn.structured.security.entity.StructureAuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现
 * 提供用户认证和信息加载
 *
 * @author chuck
 */
@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService {

    // 模拟数据库中的用户
    private static final Map<String, String> USERS = new HashMap<>();

    static {
        USERS.put("admin", "admin123");
        USERS.put("user", "user123");
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user: {}", username);
        
        String rawPassword = USERS.get(username);
        if (rawPassword == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        StructureAuthUser authUser = new StructureAuthUser();
        authUser.setId(username);
        authUser.setUsername(username);
        authUser.setPassword(passwordEncoder.encode(rawPassword));
        authUser.setEnable(true);
        authUser.setUnlocked(true);
        authUser.setUnexpired(true);
        authUser.setCreateTime(LocalDateTime.now());
        authUser.setUpdateTime(LocalDateTime.now());
        
        // 根据用户名分配不同的权限
        List<GrantedAuthority> authorities;
        if ("admin".equals(username)) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if ("user".equals(username)) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities = Collections.emptyList();
        }
        authUser.setAuthorities(authorities);
        
        log.info("User loaded successfully: {} with authorities: {}", username, authorities);
        return authUser;
    }
}
