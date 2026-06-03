package cn.structure.starter.jwt.interfaces;


import cn.structured.security.entity.StructureAuthUser;

/**
 * create by chuck 2024/6/25
 *
 * @author chuck
 * @since JDK1.8
 */
public interface ITokenStore {

    StructureAuthUser getUser(String token);

    String setUser(StructureAuthUser user);

    String refreshToken(String refreshToken);

    void clearStore(String token);
}
