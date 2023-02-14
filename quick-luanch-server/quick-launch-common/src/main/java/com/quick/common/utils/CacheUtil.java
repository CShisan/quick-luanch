package com.quick.common.utils;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author CShisan
 */
public class CacheUtil {
    /**
     * 更新
     *
     * @param key    缓存KEY
     * @param delete 删除缓存方法
     * @param update DB更新方法
     */
    public static void put(String key, Consumer<String> delete, Consumer<String> update) {
        update.accept(key);
        delete.accept(key);
    }

    /**
     * 缓存map
     *
     * @param db 缓存查询不存在, 查询数据库方法
     */
    public static <T, R> Map<T, R> cacheMap(String key, RedisUtil redisUtil, Supplier<Map<T, R>> db) {
        return db(db).cache(key, redisUtil::entities).ifNotPresent(redisUtil::putAll);
    }

    public static <T> Cache<T> db(Supplier<T> db) {
        return new Cache<>(db);
    }

    @AllArgsConstructor
    public static class Cache<T> {
        private final Supplier<T> db;

        public IfNotPresent<T> cache(String key, Function<String, T> function) {
            T cache = function.apply(key);
            return new IfNotPresent<>(key, cache, db);
        }
    }

    @AllArgsConstructor
    public static class IfNotPresent<T> {
        private final String key;
        private final T cache;
        private final Supplier<T> db;

        public T ifNotPresent(BiConsumer<String, T> consumer) {
            if (CheckUtil.isEmpty(cache)) {
                T data = db.get();
                consumer.accept(key, data);
                return data;
            }
            return cache;
        }
    }
}
