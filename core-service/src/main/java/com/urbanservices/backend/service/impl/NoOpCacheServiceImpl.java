package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.service.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * No-op cache service used when Redis is not available (local dev).
 * Always returns cache misses — all data is served from MySQL.
 */
@Service
@ConditionalOnMissingBean(RedisCacheServiceImpl.class)
public class NoOpCacheServiceImpl implements CacheService {

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public void set(String key, Object value, Duration ttl) {
        // no-op
    }

    @Override
    public boolean hasKey(String key) {
        return false;
    }

    @Override
    public void delete(String key) {
        // no-op
    }

    @Override
    public void deletePattern(String pattern) {
        // no-op
    }
}
