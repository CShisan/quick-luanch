package com.quick.common.aop;

import com.quick.common.aop.annotation.LogAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author yuanbai
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Pointcut("@annotation(com.quick.common.aop.annotation.LogAop)")
    public void logPointcut() {
    }

    @Around("logPointcut() && @annotation(aop)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, LogAop aop) throws Throwable {
        log.info(aop.beforeMsg());
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            log.info(aop.afterMsg());
        }
    }
}
