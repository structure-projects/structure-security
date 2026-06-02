package cn.structured.oauth.starter.resource.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 资源配置
 *
 * @author chuck
 * @since JDK1.8
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "structure.oauth.resource")
public class OauthResourceProperties {

    private String resourceId;

    private Map<String, List<String>> antMatchers;

}
