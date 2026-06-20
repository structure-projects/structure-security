package cn.structured.security.context;


import cn.structured.security.entity.UserContextEntity;import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户信息上下文
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@Slf4j
public class UserContext {

    /**
     * ThreadLocal 存储用户信息线程的上下文信息
     */
    private static final ThreadLocal<UserContextEntity> CONTEXT = new ThreadLocal<>();

    /**
     * 获取当前线程的用户信息上下文信息
     *
     * @return 用户信息上下文信息，可能为 null
     */
    public static UserContextEntity get() {
        return CONTEXT.get();
    }

    /**
     * 设置当前线程的用户信息上下文信息
     *
     * @param info 用户信息上下文信息
     */
    public static void set(UserContextEntity info) {
        if (log.isDebugEnabled()) {
            log.debug("Setting DataScope context: {}", info);
        }
        CONTEXT.set(info);
    }

    /**
     * 清除当前线程的用户信息上下文信息
     * <p>
     * 必须在请求/任务结束时调用，避免内存泄漏
     * </p>
     */
    public static void remove() {
        CONTEXT.remove();
        if (log.isDebugEnabled()) {
            log.debug("DataScope context cleared");
        }
    }
}
