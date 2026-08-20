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

package cn.structured.security.common.dto.social;

import lombok.Data;

/**
 * 社交登录请求
 * 支持多种社交平台：微信、钉钉等
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class SocialLoginRequest {

    /**
     * 平台编码
     * 如：weChat、dingTalk
     */
    private String platformCode;

    /**
     * 授权码
     * 第三方平台返回的授权码
     */
    private String authCode;

    /**
     * 扩展参数
     * 可选的JSON字符串，用于传递额外参数
     */
    private String extraParams;
}