package cn.structured.security.basicauth.server.sample.service;

import cn.structured.security.entity.StructureAuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Map<String, String> USERS = new HashMap<>();
    
    static {
        USERS.put("admin", "admin123");
        USERS.put("user", "user123");
        USERS.put("guest", "guest123");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = USERS.get(username);
        if (password == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        StructureAuthUser authUser = new StructureAuthUser();
        authUser.setId(username);
        authUser.setUsername(username);
        authUser.setPassword(password);
        authUser.setEnable(true);
        authUser.setUnlocked(true);
        authUser.setUnexpired(true);
        authUser.setCreateTime(LocalDateTime.now());
        authUser.setUpdateTime(LocalDateTime.now());

        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        authUser.setAuthorities(authorities);

        log.info("Loaded user: {} with authorities: {}", username, authorities);
        return authUser;
    }
}
