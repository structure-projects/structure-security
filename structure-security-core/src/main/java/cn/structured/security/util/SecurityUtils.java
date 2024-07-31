package cn.structured.security.util;


import cn.structure.common.constant.AuthConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * <p>
 * 安全工具类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/4/2 17:25
 */
@UtilityClass
public class SecurityUtils {
    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     *
     * @param authentication 认证上下文
     * @return T 当前用户登录信息
     */
    public <T> T getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return (T) principal;
    }

    /**
     * 获取用户
     */
    public <T> T getUser() {
        Authentication authentication = getAuthentication();
        return getUser(authentication);
    }

    public Long getUserId() {
        JSONObject authUser = JSON.parseObject(JSON.toJSONString(SecurityUtils.getUser()));
        Long userId = authUser.getLong(AuthConstant.USER_ID);
        return null != userId ? userId : authUser.getLong("id");
    }
}
