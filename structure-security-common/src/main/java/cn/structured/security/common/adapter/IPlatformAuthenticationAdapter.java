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

package cn.structured.security.common.adapter;

import cn.structured.security.common.dto.social.SocialUserInfo;

/**
 * 平台认证适配器
 * 定义第三方平台认证的标准接口
 *
 * @author chuck
 * @since JDK1.8
 */
public interface IPlatformAuthenticationAdapter {

    /**
     * 认证授权码
     * 使用授权码换取平台用户ID
     *
     * @param code 授权码
     * @return 平台用户ID
     */
    String authentication(String code);

    /**
     * 获取平台用户信息
     *
     * @param platformUserId 平台用户ID
     * @return 平台用户信息
     */
    SocialUserInfo getUserInfo(String platformUserId);

    /**
     * 获取平台编码
     *
     * @return 平台唯一编码
     */
    String getPlatformCode();
}