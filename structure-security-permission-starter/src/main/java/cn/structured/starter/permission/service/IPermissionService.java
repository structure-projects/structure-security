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

package cn.structured.starter.permission.service;

import cn.structured.security.entity.UserPerm;import java.util.Set;

/**
 * 权限服务接口
 * 
 * <p>提供权限检查功能，支持通配符权限匹配</p>
 * 
 * <p>使用示例：
 * <pre>
 * {@code
 * @Autowired
 * private IPermissionService permissionService;
 * 
 * if (permissionService.hasPermission("order:create")) {
 *     // 执行需要权限的操作
 * }
 * }
 * </pre>
 * </p>
 */
public interface IPermissionService {

    /**
     * 检查是否具有指定权限
     * 
     * @param permission 权限字符串（如 "order:create", "system:order:read"）
     * @return true 表示有权限，false 表示无权限
     */
    boolean hasPermission(String permission);

    /**
     * 获取当前用户的权限集合
     * 
     * @return 用户权限集合
     */
    Set<UserPerm> getUserPermissions();
}