package com.quick.common.aop.annotation;

import com.quick.common.enums.RedisKeyEnum;

import java.lang.annotation.*;

/**
 * @author yuanbai
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheDelete {
    /**
     * key集合
     */
    RedisKeyEnum[] value() default {};
}
