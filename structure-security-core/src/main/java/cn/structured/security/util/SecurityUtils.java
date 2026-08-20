/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structured.security.util;


import cn.structure.common.constant.AuthConstant;
import cn.structured.security.entity.StructureAuthUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;


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

    /**
     * 从 SecurityContext 中提取 userId。
     * <p>优先从 StructureAuthUser#id 直接读取（类型安全），
     * 兜底通过 FastJSON 序列化后按 key 查找。</p>
     *
     * @return userId，如果无法获取返回 null
     */
    public Long getUserId() {
        try {
            Object principal = SecurityUtils.getUser();
            // 优先：直接读取 StructureAuthUser.id（类型安全，不依赖 FastJSON 序列化）
            if (principal instanceof StructureAuthUser authUser) {
                return resolveUserIdFromSerializable(authUser.getId());
            }
            // 兜底：FastJSON 序列化后按 key 查找
            JSONObject authUser = JSON.parseObject(JSON.toJSONString(principal));
            Long userId = authUser.getLong(AuthConstant.USER_ID);
            return null != userId ? userId : authUser.getLong("id");
        } catch (Exception e) {
            return null;
        }
    }

    public String getStrUserId() {
        try {
            Object principal = SecurityUtils.getUser();
            // 优先：直接读取 StructureAuthUser.id
            if (principal instanceof StructureAuthUser authUser) {
                Serializable id = authUser.getId();
                return id != null ? id.toString() : null;
            }
            // 兜底：FastJSON 序列化
            JSONObject authUser = JSON.parseObject(JSON.toJSONString(principal));
            String userId = authUser.getString(AuthConstant.USER_ID);
            return null != userId ? userId : authUser.getString("id");
        } catch (Exception e) {
            return null;
        }
    }

    public StructureAuthUser getAuthUser() {
        try {
            Object principal = SecurityUtils.getUser();
            if (principal instanceof StructureAuthUser authUser) {
                return authUser;
            }
            return JSON.parseObject(JSON.toJSONString(principal), StructureAuthUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Serializable id 中安全提取 Long 值
     */
    private Long resolveUserIdFromSerializable(Serializable id) {
        if (id == null) {
            return null;
        }
        if (id instanceof Number number) {
            return number.longValue();
        }
        if (id instanceof String str) {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}