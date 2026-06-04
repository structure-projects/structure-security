package cn.structured.security.oauth.sdk.configuration;

import cn.structured.security.oauth.sdk.client.AuthClient;
import cn.structured.security.oauth.sdk.service.IRemoteClientService;
import cn.structured.security.oauth.sdk.service.impl.RemoteClientServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * 自动装配客户端配置
 *
 * @author chuck
 * @since JDK1.8
 */
@Configuration
@ComponentScan(basePackages = "cn.structured.security.oauth.sdk.**")
@EnableConfigurationProperties({AuthClientConfig.class})
public class AutoClientConfiguration {

    @Resource
    private AuthClientConfig authClientConfig;

    @Bean
    @ConditionalOnClass(AuthClient.class)
    public AuthClient authClient() {
        return new AuthClient(authClientConfig);
    }

    @Bean
    @ConditionalOnClass(IRemoteClientService.class)
    public IRemoteClientService remoteClientService() {
        return new RemoteClientServiceImpl();
    }
}
