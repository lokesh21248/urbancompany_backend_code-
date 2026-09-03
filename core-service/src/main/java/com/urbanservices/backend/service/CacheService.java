package com.urbanservices.backend.service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

public interface CacheService {

    /**
     * Retrieve a cached object by key and deserializes it to the target class.
     *
     * @param key cache key
     * @param clazz target class type
     * @param <T> value type
     * @return Optional containing the cached value, or empty if not found or Redis is offline
     */
    <T> Optional<T> get(String key, Class<T> clazz);

    /**
     * Store a value in the cache with a specific time-to-live.
     *
     * @param key cache key
     * @param value object to store
     * @param ttl time to live
     */
    void set(String key, Object value, Duration ttl);

    /**
     * Check if a key exists in cache.
     *
     * @param key cache key
     * @return true if key exists
     */
    boolean hasKey(String key);

    /**
     * Remove an entry from cache by key.
     *
     * @param key cache key
     */
    void delete(String key);

    /**
     * Evict multiple cache entries matching a glob pattern (e.g., "categories:*").
     *
     * @param pattern key pattern to evict
     */
    void deletePattern(String pattern);
}
