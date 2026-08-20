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
 * 平台编码常量
 *
 * @author chuck
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum PlatformCodeEnum {

    PHONE("phone"),
    EMAIL("email"),
    DING_TALK("dingTalk"),
    WE_CHAT("weChat");

    private String code;

}
