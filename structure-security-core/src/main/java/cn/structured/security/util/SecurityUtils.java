package cn.structured.security.util;


import cn.structure.common.constant.AuthConstant;
import cn.structured.security.entity.StructureAuthUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@UtilityClass
public class SecurityUtils {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public <T> T getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return (T) principal;
    }

    public <T> T getUser() {
        Authentication authentication = getAuthentication();
        return getUser(authentication);
    }

    public Long getUserId() {
        try {
            JSONObject authUser = JSON.parseObject(JSON.toJSONString(SecurityUtils.getUser()));
            Long userId = authUser.getLong(AuthConstant.USER_ID);
            return null != userId ? userId : authUser.getLong("id");
        }catch (Exception e) {
            return null;
        }
    }

    public String getStrUserId() {
        try {
            JSONObject authUser = JSON.parseObject(JSON.toJSONString(SecurityUtils.getUser()));
            String userId = authUser.getString(AuthConstant.USER_ID);
            return null != userId ? userId : authUser.getString("id");
        }catch (Exception e) {
            return null;
        }
    }

    public StructureAuthUser getAuthUser() {
        try {
            return JSON.parseObject(JSON.toJSONString(SecurityUtils.getUser()), StructureAuthUser.class);
        }catch (Exception e){
            return null;
        }
    }
}