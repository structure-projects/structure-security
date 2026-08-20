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
 * 异常
 *
 * @author chuck
 * @version 2024/07/24 下午5:00
 * @since 1.8
 */
@Getter
@AllArgsConstructor
public enum ErrAuthEnum {
    ERR_VERIFICATION_CODE("0201", "verification code error！"),
    ERR_PASSWORD_CODE("0202", "password error！"),
    ERR_WECHAT_AUTH_CODE("0203", "wechat authorization fail！"),
    ERR_CLIENT_AUTH_CODE("0204", "illegal client！"),
    ERR_PLATFORM_CODE("0205", "platform authentication fail！"),
    ERR_AUTHORIZE_CODE("0206", "user authorization fail！"),
    ERR_REQUEST_ID_CODE("0207", "qr code invalid！"),
    ;
    /**
     * code
     */
    private final String code;
    /**
     * message
     */
    private final String message;
}
