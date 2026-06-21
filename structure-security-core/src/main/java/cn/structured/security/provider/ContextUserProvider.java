package cn.structured.security.provider;

import cn.structured.security.context.UserContext;
import cn.structured.security.entity.StructureAuthUser;
import cn.structured.security.entity.UserContextEntity;
import cn.structured.security.interfaces.IUserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认用户信息提供者
 *
 * <p>从 Spring Security 上下文获取用户信息并设置到 UserContext</p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
@Slf4j
public class ContextUserProvider implements IUserProvider {

    @Override
    public UserContextEntity loadUser(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("User not authenticated in SecurityContext");
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof StructureAuthUser authUser) {
            UserContextEntity user = convertToUserContextEntity(authUser);
            UserContext.set(user);
            log.debug("User loaded from SecurityContext: {}", userId);
            return user;
        }

        log.debug("Principal is not StructureAuthUser type");
        return null;
    }

    private UserContextEntity convertToUserContextEntity(StructureAuthUser authUser) {
        Set<String> permissions = null;
        permissions = authUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return UserContextEntity.builder()
                .userId(authUser.getId() != null ? authUser.getId().toString() : null)
                .permissions(permissions)
                .loginTime(LocalDateTime.now())
                .build();
    }
}