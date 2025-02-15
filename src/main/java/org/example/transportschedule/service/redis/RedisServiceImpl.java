package org.example.transportschedule.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Сервис для работы с Redis.
 * Предоставляет методы для добавления, удаления, получения и очистки данных в Redis.
 */
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Добавляет данные в Redis с указанным ключом и временем жизни.
     *
     * @param key   Ключ, по которому будут сохранены данные.
     * @param value Данные для сохранения.
     * @param time  Время жизни данных в минутах.
     */
    @Override
    public void addToRedis(String key, Object value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }

    /**
     * Удаляет данные из Redis по указанному ключу.
     *
     * @param key Ключ, по которому нужно удалить данные.
     */
    @Override
    public void removeFromRedis(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Очищает все данные, связанные с Pageable, по указанному ключу.
     *
     * @param key Ключ, по которому нужно очистить данные.
     */
    @Override
    public void clearPageable(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * Получает данные из Redis по указанному ключу и преобразует их в указанный тип.
     *
     * @param key   Ключ, по которому нужно получить данные.
     * @param clazz Класс, в который нужно преобразовать данные.
     * @param <T>   Тип данных, в который нужно преобразовать.
     * @return Данные, преобразованные в указанный тип, или null, если данные не найдены.
     */
    public <T> T getFromRedis(String key, Class<T> clazz) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        }
        return objectMapper.convertValue(obj, clazz);
    }

    /**
     * Получает все данные из Redis, соответствующие указанному ключу, и преобразует их в указанный тип.
     *
     * @param key   Ключ, по которому нужно получить данные.
     * @param clazz Класс, в который нужно преобразовать данные.
     * @param <T>   Тип данных, в который нужно преобразовать.
     * @return Список данных, преобразованных в указанный тип, или пустой список, если данные не найдены.
     */
    public <T> List<T> getAll(String key, Class<T> clazz) {
        Set<String> keys = redisTemplate.keys(key + "*");
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
        if (objects == null) {
            return Collections.emptyList();
        }

        return objects.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }
}