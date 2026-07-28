package cn.structured.security.cache;

import cn.structured.security.entity.UserContextEntity;

public interface IUserContextCache {


    /**
     * 设置用户信息
     *
     * @param suerId 用户ID
     */
    void set(String suerId, UserContextEntity userContextEntity);

    /**
     * 获取用户信息
     *
     * @param suerId 用户ID
     * @return 用户信息
     */
    UserContextEntity get(String suerId);

    /**
     * 删除用户信息
     *
     * @param suerId 用户ID
     */
    void remove(String suerId);
}
