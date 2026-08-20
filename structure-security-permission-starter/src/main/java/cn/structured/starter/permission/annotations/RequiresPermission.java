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

package cn.structured.starter.permission.annotations;

import java.lang.annotation.*;

/**
 * 权限注解
 * 
 * <p>用于方法级别的权限控制，标注在需要权限的方法上</p>
 * 
 * <p>使用示例：
 * <pre>
 * {@code
 * @RequiresPermission("order:create")
 * public void createOrder() {
 *     // 需要 order:create 权限
 * }
 * 
 * @RequiresPermission("system:user:read")
 * public User getUser(Long id) {
 *     // 需要 system:user:read 权限
 * }
 * }
 * </pre>
 * </p>
 * 
 * <p>注意：该注解需要配合 Spring Security 的方法安全配置使用</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {

    /**
     * 需要的权限字符串
     * 
     * @return 权限字符串（如 "order:create"）
     */
    String value();
}