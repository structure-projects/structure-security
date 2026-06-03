package cn.structured.starter.permission;

import cn.structured.security.permission.IPermissionService;
import cn.structured.security.permission.PermissionMatcher;
import cn.structured.security.permission.UserPerm;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 权限匹配器测试
 */
public class PermissionMatcherTest {

    @Test
    public void testMatchPart() {
        assertTrue(PermissionMatcher.matchPart("order", "*"));
        assertTrue(PermissionMatcher.matchPart("order", "order"));
        assertFalse(PermissionMatcher.matchPart("order", "user"));
    }

    @Test
    public void testMatchTwoLevelExactPermission() {
        UserPerm userPerm = UserPerm.of("order", "create");
        
        assertTrue(PermissionMatcher.match(userPerm, "order:create"));
        assertFalse(PermissionMatcher.match(userPerm, "order:delete"));
        assertFalse(PermissionMatcher.match(userPerm, "user:create"));
    }

    @Test
    public void testMatchTwoLevelResourceWildcard() {
        UserPerm userPerm = UserPerm.of("order", "*");
        
        assertTrue(PermissionMatcher.match(userPerm, "order:create"));
        assertTrue(PermissionMatcher.match(userPerm, "order:delete"));
        assertTrue(PermissionMatcher.match(userPerm, "order:read"));
        assertFalse(PermissionMatcher.match(userPerm, "user:read"));
    }

    @Test
    public void testMatchTwoLevelActionWildcard() {
        UserPerm userPerm = UserPerm.of("*", "read");
        
        assertTrue(PermissionMatcher.match(userPerm, "order:read"));
        assertTrue(PermissionMatcher.match(userPerm, "user:read"));
        assertTrue(PermissionMatcher.match(userPerm, "product:read"));
        assertFalse(PermissionMatcher.match(userPerm, "order:create"));
    }

    @Test
    public void testMatchTwoLevelSuperPermission() {
        UserPerm userPerm = UserPerm.of("*", "*");
        
        assertTrue(PermissionMatcher.match(userPerm, "order:create"));
        assertTrue(PermissionMatcher.match(userPerm, "user:read"));
        assertTrue(PermissionMatcher.match(userPerm, "product:delete"));
    }

    @Test
    public void testMatchThreeLevelExactPermission() {
        UserPerm userPerm = UserPerm.of("system", "order", "create");
        
        assertTrue(PermissionMatcher.match(userPerm, "system:order:create"));
        assertFalse(PermissionMatcher.match(userPerm, "system:order:delete"));
        assertFalse(PermissionMatcher.match(userPerm, "system:user:create"));
        assertFalse(PermissionMatcher.match(userPerm, "order:create"));
    }

    @Test
    public void testMatchThreeLevelResourceWildcard() {
        UserPerm userPerm = UserPerm.of("system", "order", "*");
        
        assertTrue(PermissionMatcher.match(userPerm, "system:order:create"));
        assertTrue(PermissionMatcher.match(userPerm, "system:order:delete"));
        assertTrue(PermissionMatcher.match(userPerm, "system:order:read"));
        assertFalse(PermissionMatcher.match(userPerm, "system:user:create"));
    }

    @Test
    public void testMatchThreeLevelMiddleWildcard() {
        UserPerm userPerm = UserPerm.of("system", "*", "read");
        
        assertTrue(PermissionMatcher.match(userPerm, "system:order:read"));
        assertTrue(PermissionMatcher.match(userPerm, "system:user:read"));
        assertTrue(PermissionMatcher.match(userPerm, "system:product:read"));
        assertFalse(PermissionMatcher.match(userPerm, "system:order:create"));
    }

    @Test
    public void testMatchThreeLevelSuperPermission() {
        UserPerm userPerm = UserPerm.of("*", "*", "*");
        
        assertTrue(PermissionMatcher.match(userPerm, "system:order:create"));
        assertTrue(PermissionMatcher.match(userPerm, "system:user:read"));
        assertTrue(PermissionMatcher.match(userPerm, "module:resource:action"));
    }

    @Test
    public void testMatchDifferentLevels() {
        UserPerm twoLevelPerm = UserPerm.of("order", "*");
        UserPerm threeLevelPerm = UserPerm.of("system", "order", "*");
        
        assertFalse(PermissionMatcher.match(twoLevelPerm, "system:order:create"));
        assertFalse(PermissionMatcher.match(threeLevelPerm, "order:create"));
    }

    @Test
    public void testHasPermWithResourceWildcard() {
        Set<UserPerm> perms = new HashSet<>();
        perms.add(UserPerm.of("order", "*"));
        
        assertTrue(PermissionMatcher.hasPerm(perms, "order:create"));
        assertTrue(PermissionMatcher.hasPerm(perms, "order:delete"));
        assertFalse(PermissionMatcher.hasPerm(perms, "user:read"));
    }

    @Test
    public void testHasPermWithMultiplePermissions() {
        Set<UserPerm> perms = new HashSet<>();
        perms.add(UserPerm.of("order", "*"));
        perms.add(UserPerm.of("user", "read"));
        perms.add(UserPerm.of("system", "config", "edit"));
        
        assertTrue(PermissionMatcher.hasPerm(perms, "order:create"));
        assertTrue(PermissionMatcher.hasPerm(perms, "order:delete"));
        assertTrue(PermissionMatcher.hasPerm(perms, "user:read"));
        assertTrue(PermissionMatcher.hasPerm(perms, "system:config:edit"));
        assertFalse(PermissionMatcher.hasPerm(perms, "user:create"));
        assertFalse(PermissionMatcher.hasPerm(perms, "system:config:delete"));
    }

    @Test
    public void testUserPermParse() {
        UserPerm twoLevel = UserPerm.parse("order:create");
        assertNotNull(twoLevel);
        assertEquals(2, twoLevel.getLevel());
        assertEquals("order", twoLevel.getPart(0));
        assertEquals("create", twoLevel.getPart(1));

        UserPerm threeLevel = UserPerm.parse("system:order:create");
        assertNotNull(threeLevel);
        assertEquals(3, threeLevel.getLevel());
        assertEquals("system", threeLevel.getPart(0));
        assertEquals("order", threeLevel.getPart(1));
        assertEquals("create", threeLevel.getPart(2));

        assertNull(UserPerm.parse(null));
        assertNull(UserPerm.parse(""));
    }

    @Test
    public void testUserPermToString() {
        UserPerm twoLevel = UserPerm.of("order", "create");
        assertEquals("order:create", twoLevel.toString());

        UserPerm threeLevel = UserPerm.of("system", "order", "create");
        assertEquals("system:order:create", threeLevel.toString());
    }
}