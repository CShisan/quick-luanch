package com.quick.common.aop.annotation;

import java.lang.annotation.*;

/**
 * @author yuanbai
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAop {
    String beforeMsg() default "================================ 开始 ================================";

    String afterMsg() default "================================ 结束 ================================";
}
