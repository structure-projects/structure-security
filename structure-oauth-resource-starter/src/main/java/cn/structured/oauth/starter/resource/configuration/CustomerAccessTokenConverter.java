package cn.structured.oauth.starter.resource.configuration;

/**
 * <p>
 * 旧的 Token 转换器（已过时，不再使用）
 * 在新的 Spring Security Resource Server 架构中，请使用 JwtAuthenticationConverter
 * </p>
 *
 * @author chuck
 * @deprecated 使用 JwtAuthenticationConverter 替代
 */
@Deprecated
public class CustomerAccessTokenConverter {

    // 这个类在新架构中已不再使用
    // 相关功能已通过 JwtAuthenticationConverter 和 JwtDecoder 实现
}
