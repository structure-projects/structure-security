package cn.structure.starter.oauth.common.interfaces;


import cn.structured.security.entity.StructureAuthUser;

/**
 * create by chuck 2024/6/25
 *
 * @author chuck
 * @since JDK1.8
 * @deprecated 新的 Spring Security OAuth2 架构不再需要自定义的 TokenStore
 */
@Deprecated
public interface ITokenStore {

    StructureAuthUser getUser(String token);

    String setUser(StructureAuthUser user);

    String refreshToken(String refreshToken);

    void clearStore(String token);
}
