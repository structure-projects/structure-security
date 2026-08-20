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
