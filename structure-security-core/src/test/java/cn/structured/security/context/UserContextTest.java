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

package cn.structured.security.context;

import cn.structured.security.entity.UserContextEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户上下文测试
 */
public class UserContextTest {

    @AfterEach
    public void tearDown() {
        UserContext.remove();
    }

    @Test
    public void testSetAndGet() {
        UserContextEntity user = createUser("user1", "dept1", "tenant1");
        UserContext.set(user);

        UserContextEntity result = UserContext.get();
        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        assertEquals("dept1", result.getDeptId());
        assertEquals("tenant1", result.getTenantId());
    }

    @Test
    public void testGetWhenNotSet() {
        UserContextEntity result = UserContext.get();
        assertNull(result);
    }

    @Test
    public void testRemove() {
        UserContextEntity user = createUser("user1", "dept1", "tenant1");
        UserContext.set(user);
        UserContext.remove();

        UserContextEntity result = UserContext.get();
        assertNull(result);
    }

    @Test
    public void testThreadIsolation() throws InterruptedException {
        UserContextEntity user1 = createUser("user1", "dept1", "tenant1");
        UserContext.set(user1);

        Thread thread = new Thread(() -> {
            UserContextEntity threadUser = createUser("user2", "dept2", "tenant2");
            UserContext.set(threadUser);

            UserContextEntity threadResult = UserContext.get();
            assertNotNull(threadResult);
            assertEquals("user2", threadResult.getUserId());
            assertEquals("dept2", threadResult.getDeptId());

            UserContext.remove();
        });

        thread.start();
        thread.join();

        UserContextEntity mainResult = UserContext.get();
        assertNotNull(mainResult);
        assertEquals("user1", mainResult.getUserId());
        assertEquals("dept1", mainResult.getDeptId());
    }

    @Test
    public void testSetNull() {
        UserContext.set(null);
        assertNull(UserContext.get());
    }

    @Test
    public void testMultipleSetOverwrites() {
        UserContextEntity user1 = createUser("user1", "dept1", "tenant1");
        UserContext.set(user1);

        UserContextEntity user2 = createUser("user2", "dept2", "tenant2");
        UserContext.set(user2);

        UserContextEntity result = UserContext.get();
        assertNotNull(result);
        assertEquals("user2", result.getUserId());
        assertEquals("dept2", result.getDeptId());
    }

    private UserContextEntity createUser(String userId, String deptId, String tenantId) {
        Set<String> deptIds = new HashSet<>();
        deptIds.add(deptId);

        Set<String> roles = new HashSet<>();
        roles.add("role1");

        Set<String> permissions = new HashSet<>();
        permissions.add("perm1");

        return UserContextEntity.builder()
                .userId(userId)
                .deptId(deptId)
                .tenantId(tenantId)
                .deptIds(deptIds)
                .roles(roles)
                .permissions(permissions)
                .loginTime(LocalDateTime.now())
                .build();
    }
}