package cn.structured.starter.permission.provider;

import cn.structured.security.permission.IPermissionProvider;
import cn.structured.security.permission.UserPerm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 远程权限提供者
 * 
 * <p>通过 HTTP 请求从远程授权服务器获取用户权限</p>
 * 
 * <p>使用场景：
 * <ul>
 *   <li>需要实时获取权限信息</li>
 *   <li>权限信息存储在独立的授权服务中</li>
 *   <li>需要集中管理权限策略</li>
 * </ul>
 * </p>
 * 
 * <p>配置方式：
 * <pre>
 * {@code
 * structure:
 *   security:
 *     permission:
 *       providerType: remote
 *       remoteUrl: https://auth-server/api/permissions/{userId}
 * }
 * </pre>
 * </p>
 * 
 * <p>远程接口要求：
 * <ul>
 *   <li>HTTP 方法：GET</li>
 *   <li>URL 中 {userId} 会被实际用户ID替换</li>
 *   <li>返回格式：JSON 数组，包含权限字符串</li>
 *   <li>示例响应：["order:create", "user:read", "system:*"]</li>
 * </ul>
 * </p>
 */
@Slf4j
public class RemotePermissionProvider implements IPermissionProvider {

    private final RestTemplate restTemplate;
    private final String permissionUrl;

    /**
     * 创建远程权限提供者
     * 
     * @param restTemplate RestTemplate 实例
     * @param permissionUrl 远程权限接口 URL（包含 {userId} 占位符）
     */
    public RemotePermissionProvider(RestTemplate restTemplate, String permissionUrl) {
        this.restTemplate = restTemplate;
        this.permissionUrl = permissionUrl;
    }

    @Override
    public Set<UserPerm> getPermissions(String userId) {
        if (userId == null || userId.isEmpty()) {
            return Collections.emptySet();
        }

        try {
            String url = permissionUrl.replace("{userId}", userId);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    List.class
            );

            List<String> permissionStrings = response.getBody();
            if (permissionStrings == null || permissionStrings.isEmpty()) {
                return Collections.emptySet();
            }

            Set<UserPerm> permissions = new HashSet<>();
            for (Object obj : permissionStrings) {
                String permStr = String.valueOf(obj);
                UserPerm perm = UserPerm.parse(permStr);
                if (perm != null) {
                    permissions.add(perm);
                }
            }
            return permissions;

        } catch (Exception e) {
            log.error("Failed to fetch permissions from remote server for user {}: {}", userId, e.getMessage());
            return Collections.emptySet();
        }
    }
}