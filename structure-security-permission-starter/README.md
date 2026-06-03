# Structure Security Permission Module

权限模块提供基于通配符的功能权限管理，支持多层级权限结构和多种权限获取方式。

## 功能特性

- **结构化权限模型**：支持二层和三层权限结构
- **通配符匹配**：支持资源级、动作级和超级权限通配
- **多种权限来源**：支持从上下文、远程服务器获取权限
- **Spring Security 集成**：无缝集成 Spring Security 框架

## 权限模型

### 权限格式

权限采用 `{resource}:{action}` 或 `{module}:{resource}:{action}` 格式。

### 通配符规则

| 权限写法 | 含义 | 示例 |
|---------|------|------|
| `order:create` | 精确匹配 | 仅匹配 `order:create` |
| `order:*` | 资源级通配 | 匹配 `order:create`, `order:delete` 等 |
| `*:read` | 动作级通配 | 匹配 `order:read`, `user:read` 等 |
| `*:*` | 超级权限 | 匹配所有二层权限 |
| `system:order:create` | 三层精确匹配 | 仅匹配 `system:order:create` |
| `system:order:*` | 三层资源级通配 | 匹配 `system:order:create` 等 |
| `system:*:read` | 三层中间通配 | 匹配 `system:order:read`, `system:user:read` 等 |
| `*:*:*` | 三层超级权限 | 匹配所有三层权限 |

### 匹配规则

1. 层级必须相同才能匹配（如 `order:*` 不匹配 `system:order:create`）
2. 每个层级支持通配符 `*` 匹配任意值
3. 不是字符串包含匹配，而是结构匹配

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-security-permission-starter</artifactId>
    <version>${structure.security.version}</version>
</dependency>
```

### 2. 配置权限提供者

```yaml
structure:
  security:
    permission:
      enabled: true
      provider-type: context  # context, remote
```

### 3. 使用权限检查

**使用原生 Spring Security 注解：**

```java
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("@permissionService.hasPermission('order:create')
public void createOrder() {
    // 需要 order:create 权限
}

@PreAuthorize("@permissionService.hasPermission('system:order:read')
public Order getOrder(Long id) {
    // 需要 system:order:read 权限
}
```

**编程方式：**

```java
import cn.structured.security.permission.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderService {
    
    @Autowired
    private IPermissionService permissionService;
    
    public void createOrder() {
        if (permissionService.hasPermission("order:create")) {
            // 有权限，执行操作
        }
    }
}
```

## 权限提供者类型

### 1. Context（默认）

从 Spring Security 上下文获取当前认证用户的权限信息。

**工作原理：**
1. 从 `SecurityContextHolder` 获取当前 `Authentication` 对象
2. 获取 `Principal`（必须是 `StructureAuthUser` 类型）
3. 从 `StructureAuthUser.getAuthorities()` 获取权限列表

**适用场景：**
- 已完成认证的请求
- 需要从用户对象直接获取权限

**配置：**

```yaml
structure:
  security:
    permission:
      provider-type: context
```

### 2. Remote

通过 HTTP 请求从远程授权服务器获取用户权限。

**配置：**

```yaml
structure:
  security:
    permission:
      provider-type: remote
      remote-url: https://auth-server/api/permissions/{userId}
```

**远程接口要求：**
- HTTP 方法：GET
- URL 中 `{userId}` 会被实际用户ID替换
- 返回格式：JSON 数组，包含权限字符串
- 示例响应：`["order:create", "user:read", "system:*"]`

**适用场景：**
- 需要实时获取权限信息
- 权限信息存储在独立的授权服务中

## 模块结构

```
structure-security-core/
├── permission/
│   ├── IPermissionProvider.java    # 权限提供者接口
│   ├── IPermissionService.java     # 权限服务接口
│   ├── PermissionMatcher.java      # 权限匹配算法
│   ├── RequiresPermission.java     # 权限注解
│   └── UserPerm.java               # 权限模型

structure-security-permission-starter/
├── permission/
│   ├── ContextPermissionProvider.java   # 上下文权限提供者
│   ├── RemotePermissionProvider.java    # 远程权限提供者
│   ├── PermissionServiceImpl.java       # 权限服务实现
│   ├── PermissionProperties.java        # 配置属性
│   └── PermissionAutoConfiguration.java # 自动配置
```

## 核心类说明

### UserPerm

结构化的权限表示，支持多层级权限结构。

```java
// 创建权限对象
UserPerm perm = UserPerm.of("order", "create");

// 从字符串解析
UserPerm perm = UserPerm.parse("order:create");

// 获取层级
int level = perm.getLevel(); // 返回 2

// 获取指定部分
String resource = perm.getPart(0); // 返回 "order"
String action = perm.getPart(1);   // 返回 "create"
```

### PermissionMatcher

权限匹配器，实现通配符匹配算法。

```java
Set<UserPerm> perms = new HashSet<>();
perms.add(UserPerm.of("order", "*"));

// 检查权限
boolean hasPerm = PermissionMatcher.hasPerm(perms, "order:create"); // 返回 true
boolean hasPerm = PermissionMatcher.hasPerm(perms, "user:read");    // 返回 false
```

### IPermissionService

权限服务接口，提供权限检查功能。

```java
boolean hasPermission(String permission);  // 检查是否具有指定权限
Set<UserPerm> getUserPermissions();        // 获取当前用户的权限集合
```

## 配置属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `enabled` | boolean | true | 是否启用权限模块 |
| `provider-type` | string | context | 权限提供者类型 |
| `remote-url` | string | - | 远程权限接口 URL（remote 模式） |

## 扩展自定义权限提供者

如果需要自定义权限获取方式，可以实现 `IPermissionProvider` 接口：

```java
public class CustomPermissionProvider implements IPermissionProvider {
    
    @Override
    public Set<UserPerm> getPermissions(String userId) {
        // 自定义权限获取逻辑
        Set<UserPerm> permissions = new HashSet<>();
        // ...
        return permissions;
    }
}
```

然后在配置类中注册为 Bean：

```java
@Bean
public IPermissionProvider customPermissionProvider() {
    return new CustomPermissionProvider();
}
```

## 缓存机制（Remote 模式）

### 缓存策略

远程模式下，每次请求都会调用远程权限服务获取权限，这可能导致性能问题。建议在生产环境中实现缓存机制。

### 推荐的缓存实现

```java
public class CachedRemotePermissionProvider implements IPermissionProvider {

    private final RemotePermissionProvider delegate;
    private final Map<String, CachedPermissions> cache = new ConcurrentHashMap<>();
    
    // 缓存过期时间（默认5分钟）
    private static final long CACHE_EXPIRE_MS = 5 * 60 * 1000;

    public CachedRemotePermissionProvider(RestTemplate restTemplate, String permissionUrl) {
        this.delegate = new RemotePermissionProvider(restTemplate, permissionUrl);
    }

    @Override
    public Set<UserPerm> getPermissions(String userId) {
        if (userId == null) {
            return Collections.emptySet();
        }

        CachedPermissions cached = cache.get(userId);
        
        // 检查缓存是否有效
        if (cached != null && !cached.isExpired()) {
            return cached.getPermissions();
        }

        // 缓存失效，从远程获取
        Set<UserPerm> permissions = delegate.getPermissions(userId);
        
        // 更新缓存
        cache.put(userId, new CachedPermissions(permissions));
        
        return permissions;
    }

    /**
     * 手动刷新指定用户的权限缓存
     */
    public void refreshCache(String userId) {
        cache.remove(userId);
    }

    /**
     * 手动刷新所有用户的权限缓存
     */
    public void refreshAllCache() {
        cache.clear();
    }

    private static class CachedPermissions {
        private final Set<UserPerm> permissions;
        private final long timestamp;

        public CachedPermissions(Set<UserPerm> permissions) {
            this.permissions = permissions;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_EXPIRE_MS;
        }

        public Set<UserPerm> getPermissions() {
            return permissions;
        }
    }
}
```

### 缓存配置建议

| 场景 | 缓存策略 | 过期时间建议 |
|------|---------|-------------|
| 开发环境 | 禁用缓存 | 0 |
| 测试环境 | 短缓存 | 1-5分钟 |
| 生产环境 | 中长缓存 | 5-30分钟 |
| 高安全要求 | 短缓存或禁用 | 1-5分钟 |

## 权限更新机制

### 实时更新

权限更新通常通过以下方式实现：

1. **Token 刷新时更新**：在 JWT Token 刷新时，重新获取权限信息
2. **事件驱动更新**：通过消息队列（如 Kafka）订阅权限变更事件
3. **定时任务更新**：定期同步权限信息（适合权限变更不频繁的场景）

### 手动刷新接口示例

```java
@RestController
@RequestMapping("/admin")
public class PermissionController {

    private final CachedRemotePermissionProvider permissionProvider;

    public PermissionController(CachedRemotePermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
    }

    /**
     * 刷新指定用户的权限缓存
     */
    @PostMapping("/permissions/refresh/{userId}")
    public Map<String, Object> refreshUserPermissions(@PathVariable String userId) {
        permissionProvider.refreshCache(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "权限缓存已刷新");
        return result;
    }

    /**
     * 刷新所有用户的权限缓存
     */
    @PostMapping("/permissions/refresh-all")
    public Map<String, Object> refreshAllPermissions() {
        permissionProvider.refreshAllCache();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "所有权限缓存已刷新");
        return result;
    }
}
```

### 权限更新流程图

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ 权限变更事件     │ ──▶ │ 消息队列        │ ──▶ │ 权限缓存刷新    │
│ (数据库变更)     │     │ (Kafka/Rabbit)  │     │ (清空缓存)      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                       │
                                                       ▼
                                              ┌─────────────────┐
                                              │ 下次请求时      │
                                              │ 重新获取权限    │
                                              └─────────────────┘
```

## 测试示例

### 上下文权限测试

```java
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class ContextPermissionTest {
    
    @Autowired
    private IPermissionService permissionService;
    
    @Test
    @WithMockUser(authorities = { "order:create", "user:read" })
    public void testContextPermission() {
        assertTrue(permissionService.hasPermission("order:create"));
        assertTrue(permissionService.hasPermission("user:read"));
        assertFalse(permissionService.hasPermission("order:delete"));
    }
}
```

### 远程权限测试

```java
@SpringBootTest(properties = {
    "structure.security.permission.provider-type=remote",
    "structure.security.permission.remote-url=http://localhost:8080/permissions/{userId}"
})
@TestInstance(Lifecycle.PER_CLASS)
public class RemotePermissionTest {
    
    @Autowired
    private IPermissionService permissionService;
    
    @MockBean
    private RestTemplate restTemplate;
    
    @Test
    @WithMockUser(username = "1")
    public void testRemotePermission() {
        when(restTemplate.exchange(
            eq("http://localhost:8080/permissions/1"),
            eq(HttpMethod.GET),
            any(),
            eq(List.class)
        )).thenReturn(new ResponseEntity<>(
            Arrays.asList("order:create", "user:read"),
            HttpStatus.OK
        ));
        
        assertTrue(permissionService.hasPermission("order:create"));
    }
}
```

### 权限注解测试

```java
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class RequiresPermissionTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(authorities = { "order:create" })
    public void testRequiresPermission_allowed() throws Exception {
        mockMvc.perform(post("/order/create"))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(authorities = { "user:read" })
    public void testRequiresPermission_denied() throws Exception {
        mockMvc.perform(post("/order/create"))
            .andExpect(status().isForbidden());
    }
}
```

## 版本说明

- **1.0.0**：初始版本，支持基础权限模型和两种权限提供者

## License

Apache License 2.0
