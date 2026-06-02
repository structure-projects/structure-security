package cn.structure.starter.oauth.common.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Swagger 配置（适配新版本 Spring Boot，springfox 已废弃）
 * </p>
 *
 * @author chuck
 * @version 2024/07/16 下午8:42
 * @since 1.8
 */
@Configuration
@ConditionalOnProperty(prefix = "structure.swagger", name = "enabled", havingValue = "true", matchIfMissing = false)
public class AutoSwaggerFix {

    // 注意：Springfox 已不再维护，建议迁移到 SpringDoc OpenAPI
    // 参考：https://springdoc.org/

    // 如果还需要使用 Swagger，可以暂时注释掉旧的代码
    // 或者添加 springdoc-openapi-starter-webmvc-ui 依赖
}
