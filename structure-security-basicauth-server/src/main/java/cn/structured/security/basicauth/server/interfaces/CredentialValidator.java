package cn.structured.security.basicauth.server.interfaces;

/**
 * 凭证验证器接口
 * <p>
 * 用于验证用户名和密码是否有效
 * </p>
 *
 * @author chuck
 */
public interface CredentialValidator {

    /**
     * 验证用户名和密码
     *
     * @param username 用户名
     * @param password 密码
     * @return true 表示验证成功，false 表示验证失败
     */
    boolean validate(String username, String password);
}
