package com.urbanservices.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;

/**
 * Redis (Lettuce) configuration.
 *
 * <p>Redis is used for:
 * <ul>
 *   <li>Rate limiting (Phase 28)</li>
 *   <li>Distributed locks — booking concurrency (Phase 19/32)</li>
 *   <li>Cache — hot catalog data (Phase 28)</li>
 *   <li>Session storage — if needed</li>
 * </ul>
 *
 * <p>Uses Lettuce connection pool for connection reuse.
 * Redis must not be a single point of failure — the application must
 * degrade gracefully if Redis is temporarily unavailable for non-critical paths.
 */
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(name = "redis.enabled", havingValue = "true")
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    /**
     * General-purpose RedisTemplate<String, Object>.
     * Values are serialized as JSON using Jackson.
     * Use this for storing complex objects.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        log.info("RedisTemplate<String, Object> initialized");
        return template;
    }

    /**
     * StringRedisTemplate for simple string key/value operations.
     * Use this for counters, flags, and rate limiting.
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
