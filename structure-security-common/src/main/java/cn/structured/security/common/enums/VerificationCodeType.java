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

package cn.structured.security.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码业务类型
 *
 * @author chuck
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum VerificationCodeType {

    LOGIN("login", "用户登录验证", "您正在进行用户登录操作，当前操作的验证码为%s,验证码在5分钟内有效！"),
    REGISTER("register", "用户注册验证", "您正在进行用户注册操作，当前操作的验证码为%s,验证码在5分钟内有效！"),
    RESET_PASSWORD("resetPassword", "用户重置密码验证", "您正在进行密码重置操作，当前操作的验证码为%s,验证码在5分钟内有效！");

    private String code;

    private String name;

    private String text;
}
