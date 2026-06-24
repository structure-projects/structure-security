package cn.structured.security.cache;

import cn.structured.security.entity.UserContextEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InMemoryUserContextCache implements IUserContextCache {

    private static final Map<String, UserContextEntity> USER_CACHE = new ConcurrentHashMap<>();

    @Override
    public void set(String userId, UserContextEntity userContextEntity) {
        USER_CACHE.put(userId, userContextEntity);
    }

    @Override
    public UserContextEntity get(String userId) {
        return USER_CACHE.get(userId);
    }

    @Override
    public void remove(String suerId) {
        USER_CACHE.remove(suerId);
    }
}
