package com.quick.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author CShisan
 */
@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> template;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    // =====================================key value[start]=====================================

    public void set(String key, String value, Long exp) {
        set(key, value, exp, null);
    }

    public void set(String key, String value, Long exp, TimeUnit unit) {
        unit = Optional.ofNullable(unit).orElse(TimeUnit.MILLISECONDS);
        template.opsForValue().set(key, value, exp, unit);
    }

    public String get(String key) {
        return (String) template.opsForValue().get(key);
    }

    public void delete(String key) {
        template.delete(key);
    }

    public void batchDelete(String... key) {
        this.batchDelete(Arrays.asList(key));
    }

    public void batchDelete(Collection<String> keys) {
        template.delete(keys);
    }

    // =====================================key value[end]=======================================


    // =====================================key list[start]=====================================

    public List<Object> listAll(String key) {
        return template.opsForList().range(key, 0, -1);
    }

    @SafeVarargs
    public final <R> void rightPushAll(String key, R... values) {
        template.opsForList().rightPushAll(key, values);
    }

    // =====================================key list[end]=======================================


    // =====================================key map[start]=====================================

    public <T, R> void putAll(String key, Map<T, R> map) {
        template.opsForHash().putAll(key, map);
    }

    public <T, R> Map<T, R> entities(String key) {
        HashOperations<String, T, R> operations = template.opsForHash();
        return operations.entries(key);
    }

    // =====================================key map[end]=======================================
}
