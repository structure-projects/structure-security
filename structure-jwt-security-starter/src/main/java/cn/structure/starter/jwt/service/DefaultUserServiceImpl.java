package cn.structure.starter.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 默认用户实现类.这个是一个空实现.用于默认装配.如果使用为授权角色请自行重写
 *
 * @author cqliut
 * @version 2023.0625
 * @since 1.0.1
 */
public class DefaultUserServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
