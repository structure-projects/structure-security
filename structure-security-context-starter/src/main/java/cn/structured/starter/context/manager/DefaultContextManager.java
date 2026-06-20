package cn.structured.starter.context.manager;

import cn.structured.starter.context.store.IUserStore;

/**
 * 默认上下文管理器
 *
 * <p>注册默认的用户存储和上下文管理器 Bean</p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
public class DefaultContextManager extends AbsContextManager {
    /**
     * 构造函数
     *
     * @param userStore 用户存储
     */
    public DefaultContextManager(IUserStore userStore) {
        super(userStore);
    }
}
