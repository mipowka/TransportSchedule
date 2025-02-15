package org.example.transportschedule.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    //добавляет данные в Redis
    public void addToRedis(String key, Object value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }

    @Override
    //удаляет данные из Redis
    public void removeFromRedis(String key) {
        redisTemplate.delete(key);
    }

    @Override
    //Очищает все данные с Pageable по ключу
    public void clearPageable(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public <T> T getFromRedis(String key, Class<T> clazz) {
        // Получаем данные из Redis
        Object obj = redisTemplate.opsForValue().get(key);

        // Если данные не найдены, возвращаем null
        if (obj == null) {
            return null;
        }

        // Преобразуем объект в нужный тип
        return objectMapper.convertValue(obj, clazz);
    }

    public <T> List<T> getListFromRedis(String key, Class<T> clazz) {
        // Получаем данные из Redis
        Object obj = redisTemplate.opsForValue().get(key);

        // Если данные не найдены, возвращаем пустой список
        if (obj == null) {
            return Collections.emptyList();
        }


        // Преобразуем объект в нужный тип (список)
        return objectMapper.convertValue(obj, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
