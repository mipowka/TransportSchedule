package org.example.transportschedule.service.redis;

import java.util.List;

public interface RedisService {
    void addToRedis(String key, Object value, Long time);
    void removeFromRedis(String key);
    void clearPageable(String key);
    <T> T getFromRedis(String key, Class<T> clazz);
    <T> List<T> getListFromRedis(String key, Class<T> clazz);
}
