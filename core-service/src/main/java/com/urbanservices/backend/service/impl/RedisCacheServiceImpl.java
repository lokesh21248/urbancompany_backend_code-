package com.urbanservices.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanservices.backend.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(org.springframework.data.redis.core.RedisTemplate.class)
public class RedisCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return Optional.empty();
            }
            if (clazz.isInstance(value)) {
                return Optional.of(clazz.cast(value));
            }
            // If stored as LinkedHashMap or raw object, convert with Jackson
            return Optional.of(objectMapper.convertValue(value, clazz));
        } catch (Exception e) {
            log.warn("Cache read failed for key '{}'. Continuing with DB fallback. Error: {}", key, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void set(String key, Object value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception e) {
            log.warn("Cache write failed for key '{}'. Error: {}", key, e.getMessage());
        }
    }

    @Override
    public boolean hasKey(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.warn("Cache hasKey check failed for key '{}'. Error: {}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Cache delete failed for key '{}'. Error: {}", key, e.getMessage());
        }
    }

    @Override
    public void deletePattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Evicted {} cache keys matching pattern: {}", keys.size(), pattern);
            }
        } catch (Exception e) {
            log.warn("Cache deletePattern failed for pattern '{}'. Error: {}", pattern, e.getMessage());
        }
    }
}
