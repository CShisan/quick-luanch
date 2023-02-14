package com.quick.common.aop;

import com.quick.common.aop.annotation.CacheDelete;
import com.quick.common.enums.RedisKeyEnum;
import com.quick.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuanbai
 */
@Slf4j
@Aspect
@Component
public class CacheDeleteAspect {
    private final RedisUtil redisUtil;

    @Autowired
    public CacheDeleteAspect(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Pointcut("@annotation(com.quick.common.aop.annotation.CacheDelete)")
    public void pointcut() {
    }

    @AfterReturning("pointcut() && @annotation(annotation)")
    public void afterReturning(CacheDelete annotation) {
        log.debug("【Redis缓存删除】开始删除...");
        List<String> keys = Arrays.stream(annotation.value()).map(RedisKeyEnum::getKey).collect(Collectors.toList());
        redisUtil.batchDelete(keys);
        log.debug("【Redis缓存删除】KEY{}删除成功", keys);
    }
}
