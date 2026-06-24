package cn.structured.security.interfaces;

import cn.structured.security.entity.UserContextEntity;

/**
 * 用户信息提供者接口
 *
 * <p>定义从不同来源获取用户信息的接口，并将用户信息设置到 UserContext 中。
 * 支持多种实现方式：
 * <ul>
 *   <li>ContextUserProvider - 从 Spring Security 上下文获取</li>
 *   <li>HeaderUserProvider - 从请求头获取</li>
 *   <li>RemoteUserProvider - 从远程授权服务器获取</li>
 * </ul>
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2024/6/25
 */
public interface IUserProvider {

    /**
     * 加载用户信息
     *
     * <p>从不同来源获取用户信息并设置到 UserContext 中。
     * 具体获取方式由实现类决定（Spring Security上下文、请求头、远程服务等）。</p>
     *
     * @param sessionId 会话ID
     * @return 当前用户信息，未获取到返回null
     */
    UserContextEntity loadUser(String sessionId);
}