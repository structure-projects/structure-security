/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structure.starter.oauth.resource.example.config;

import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * bean配置（适配新的 Spring Security OAuth2 Resource Server）
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/8 15:04
 * @deprecated 新的 Spring Security OAuth2 Resource Server 不再需要 TokenStore
 */
@Configuration
@Deprecated
public class BeanConfig {

    // 注意：在新的 Spring Security OAuth2 Resource Server 架构中
    // 不再需要 TokenStore，因为现在使用 JwtDecoder 来验证 token
    // JwtDecoder 已经在 AutoResourceConfiguration 中配置了

}
