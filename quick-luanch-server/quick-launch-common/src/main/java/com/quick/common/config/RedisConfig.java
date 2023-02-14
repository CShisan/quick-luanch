package com.quick.common.config;

import com.quick.common.factory.ConfigurationSourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author yuanbai
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
@PropertySource(value = "classpath:config/redis.yml", factory = ConfigurationSourceFactory.class)
public class RedisConfig {
    private Long expiration;

    @Bean
    @Primary
    public RedisTemplate<String, Object> object(RedisConnectionFactory factory) {
        return this.redisTemplate(factory);
    }

    public <T, R> RedisTemplate<T, R> redisTemplate(RedisConnectionFactory factory) {
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置序列器
        RedisTemplate<T, R> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
