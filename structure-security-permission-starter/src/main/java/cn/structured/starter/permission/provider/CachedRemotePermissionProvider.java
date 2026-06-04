package cn.structured.starter.permission.provider;

import cn.structured.security.permission.IPermissionProvider;
import cn.structured.security.permission.UserPerm;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 带缓存的远程权限提供者
 * 
 * <p>结合远程权限获取和本地缓存，在保持实时性的同时提高性能</p>
 * 
 * <p>工作原理：
 * <ol>
 *   <li>首次请求时从远程授权服务器获取权限</li>
 *   <li>将权限缓存到本地内存</li>
 *   <li>后续请求优先从缓存读取</li>
 *   <li>缓存过期后自动从远程刷新</li>
 * </ol>
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
 *       cache:
 *         enabled: true
 *         ttl: 30m
 *         maxSize: 10000
 * }
 * </pre>
 * </p>
 * 
 * <p>缓存特性：
 * <ul>
 *   <li>基于 Caffeine 高性能缓存</li>
 *   <li>支持 TTL 过期策略</li>
 *   <li>支持最大容量限制</li>
 *   <li>提供手动刷新接口</li>
 * </ul>
 * </p>
 */
@Slf4j
public class CachedRemotePermissionProvider implements IPermissionProvider {

    private final RemotePermissionProvider remoteProvider;
    private final Cache<String, Set<UserPerm>> permissionCache;

    /**
     * 创建带缓存的远程权限提供者
     * 
     * @param remoteProvider 远程权限提供者
     * @param ttl 缓存过期时间
     * @param maxSize 缓存最大容量
     */
    public CachedRemotePermissionProvider(RemotePermissionProvider remoteProvider, Duration ttl, int maxSize) {
        this.remoteProvider = remoteProvider;
        this.permissionCache = Caffeine.newBuilder()
                .expireAfterWrite(ttl.toMillis(), TimeUnit.MILLISECONDS)
                .maximumSize(maxSize)
                .recordStats()
                .build();
        log.info("CachedRemotePermissionProvider initialized with ttl={}, maxSize={}", ttl, maxSize);
    }

    @Override
    public Set<UserPerm> getPermissions(String userId) {
        if (userId == null || userId.isEmpty()) {
            return Collections.emptySet();
        }

        try {
            Set<UserPerm> permissions = permissionCache.getIfPresent(userId);
            if (permissions != null) {
                log.debug("Cache hit for user: {}", userId);
                return permissions;
            }

            log.debug("Cache miss for user: {}, fetching from remote", userId);
            permissions = remoteProvider.getPermissions(userId);
            permissionCache.put(userId, permissions);
            return permissions;

        } catch (Exception e) {
            log.error("Failed to get permissions for user {}: {}", userId, e.getMessage());
            return Collections.emptySet();
        }
    }

    /**
     * 手动刷新指定用户的权限缓存
     * 
     * @param userId 用户ID
     */
    public void refreshPermissions(String userId) {
        if (userId == null || userId.isEmpty()) {
            return;
        }
        permissionCache.invalidate(userId);
        log.info("Permission cache invalidated for user: {}", userId);
    }

    /**
     * 刷新所有用户的权限缓存
     */
    public void refreshAllPermissions() {
        permissionCache.invalidateAll();
        log.info("All permission caches invalidated");
    }

    /**
     * 获取缓存统计信息
     * 
     * @return 缓存统计信息
     */
    public CacheStats getCacheStats() {
        com.github.benmanes.caffeine.cache.stats.CacheStats stats = permissionCache.stats();
        return new CacheStats(
                stats.hitCount(),
                stats.missCount(),
                stats.hitRate(),
                permissionCache.estimatedSize()
        );
    }

    /**
     * 缓存统计信息
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class CacheStats {
        private long hitCount;
        private long missCount;
        private double hitRate;
        private long size;
    }
}
