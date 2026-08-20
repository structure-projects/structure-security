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

package cn.structure.starter.jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录异常枚举类
 *
 * @author cqliut
 * @version 2023.0707
 * @since 1.0.1
 */
@Getter
@AllArgsConstructor
public enum LoginErrCodeEnum {

    USER_PASSWORD_ERR("", "用户名密码错误！"),
    USER_DISABLED("", "用户失效！"),
    USER_LOCKED_ERR("", "用户锁定！");
    private String code;

    private String msg;

}
