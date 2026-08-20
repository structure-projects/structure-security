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
 * 社交平台用户信息
 * 封装第三方平台返回的用户基本信息
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class SocialUserInfo {

    /**
     * 第三方平台用户ID
     */
    private String platformUserId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别
     * 0: 未知 1: 男 2: 女
     */
    private Integer gender;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 所在省份
     */
    private String province;

    /**
     * 所在国家
     */
    private String country;

    /**
     * 用户唯一标识
     */
    private String unionId;
}