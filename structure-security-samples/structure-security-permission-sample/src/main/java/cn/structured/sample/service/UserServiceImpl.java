package cn.structured.sample.service;

import cn.structure.starter.jwt.interfaces.ITokenService;
import cn.structured.security.entity.StructureAuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户服务实现
 * 
 * 根据用户名分配不同的权限：
 * - admin: 超级管理员，拥有所有权限
 * - user: 普通用户，拥有基础权限
 * - guest: 访客，拥有只读权限
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StructureAuthUser authUser = new StructureAuthUser();
        authUser.setId("1");
        authUser.setUsername(username);
        authUser.setPassword(passwordEncoder.encode("123456"));
        authUser.setEnable(true);
        authUser.setUnlocked(true);
        authUser.setUnexpired(true);
        authUser.setCreateTime(LocalDateTime.now());
        authUser.setUpdateTime(LocalDateTime.now());
        
        // 根据用户名分配不同的权限
        List<GrantedAuthority> authorities = getAuthoritiesByUsername(username);
        authUser.setAuthorities(authorities);
        
        return authUser;
    }

    /**
     * 根据用户名获取权限列表
     */
    private List<GrantedAuthority> getAuthoritiesByUsername(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if ("admin".equals(username)) {
            // 超级管理员：拥有所有权限
            authorities.add(new SimpleGrantedAuthority("*:*"));           // 所有二层权限
            authorities.add(new SimpleGrantedAuthority("*:*:*"));         // 所有三层权限
        } else if ("user".equals(username)) {
            // 普通用户：拥有订单和用户管理权限
            authorities.add(new SimpleGrantedAuthority("order:create"));  // 创建订单
            authorities.add(new SimpleGrantedAuthority("order:read"));    // 读取订单
            authorities.add(new SimpleGrantedAuthority("order:update"));  // 更新订单
            authorities.add(new SimpleGrantedAuthority("user:read"));     // 读取用户信息
        } else if ("guest".equals(username)) {
            // 访客：只有只读权限
            authorities.add(new SimpleGrantedAuthority("*:read"));        // 所有资源的读取权限
        } else {
            // 未知用户：无权限
            return Collections.emptyList();
        }
        
        return authorities;
    }
}