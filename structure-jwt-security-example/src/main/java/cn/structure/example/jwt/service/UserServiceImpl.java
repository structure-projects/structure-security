package cn.structure.example.jwt.service;

import cn.structured.security.entity.StructureAuthUser;
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
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/11 14:23
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        List<GrantedAuthority> authorities;
        if ("admin".equals(username)) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if ("user".equals(username)) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities = Collections.emptyList();
        }
        authUser.setAuthorities(authorities);
        return authUser;
    }
}
