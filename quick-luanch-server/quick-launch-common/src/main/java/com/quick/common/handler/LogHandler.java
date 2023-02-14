package com.quick.common.handler;

import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.CheckUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Slf4j
public class LogHandler {
    public static final String MSG_FORMAT = "【%s】 --> %s";

    /**
     * 判断不为空
     *
     * @see LogHandler#requireNotNull(BiConsumer, Object, String)
     */
    public static void requireNotNull(BiConsumer<String, Object[]> logger, Object obj) {
        requireNotNull(logger, obj, "对象不能为空");
    }

    /**
     * 判断不为空
     *
     * @see LogHandler#requireNotNull(BiConsumer, Object, String, Object...)
     */
    public static void requireNotNull(BiConsumer<String, Object[]> logger, Object obj, String tips) {
        requireNotNull(logger, obj, tips, (Object[]) null);
    }

    /**
     * 判断不为空
     */
    public static void requireNotNull(BiConsumer<String, Object[]> logger, Object obj, String tips, Object... objs) {
        assertTrue(logger, CheckUtil.nonNull(obj), tips, objs);
    }

    /**
     * 判断obj不为empty
     *
     * @see LogHandler#requireNotEmpty(BiConsumer, Object, String)
     */
    public static void requireNotEmpty(BiConsumer<String, Object[]> logger, Object obj) {
        requireNotEmpty(logger, obj, "数据长度不能为0");
    }

    /**
     * 判断obj不为empty
     *
     * @see LogHandler#requireNotEmpty(BiConsumer, Object, String, Object...)
     */
    public static void requireNotEmpty(BiConsumer<String, Object[]> logger, Object obj, String tips) {
        requireNotEmpty(logger, obj, tips, (Object[]) null);
    }


    /**
     * 判断obj不为empty
     */
    public static void requireNotEmpty(BiConsumer<String, Object[]> logger, Object obj, String tips, Object... objs) {
        assertTrue(logger, CheckUtil.nonEmpty(obj), tips, objs);
    }

    /**
     * 判断obj不为空字符串
     *
     * @see LogHandler#requireNotBlank(BiConsumer, Object, String)
     */
    public static void requireNotBlank(BiConsumer<String, Object[]> logger, Object obj) {
        requireNotBlank(logger, obj, "数据不能为空/空字符串");
    }

    /**
     * 判断obj不为空字符串
     *
     * @see LogHandler#requireNotBlank(BiConsumer, Object, String, Object...)
     */
    public static void requireNotBlank(BiConsumer<String, Object[]> logger, Object obj, String tips) {
        requireNotBlank(logger, obj, tips, (Object[]) null);
    }

    /**
     * 判断obj不为空字符串
     */
    public static void requireNotBlank(BiConsumer<String, Object[]> logger, Object obj, String tips, Object... objs) {
        assertTrue(logger, CheckUtil.nonBlank(obj), tips, objs);
    }


    /**
     * 断言状态为true
     *
     * @see LogHandler#assertTrue(BiConsumer, Boolean, String)
     */
    public static void assertTrue(BiConsumer<String, Object[]> logger, Boolean status) {
        assertTrue(logger, status, "断言true失败");
    }

    /**
     * 断言状态为true
     *
     * @see LogHandler#assertTrue(BiConsumer, Boolean, String, Object...)
     */
    public static void assertTrue(BiConsumer<String, Object[]> logger, Boolean status, String tips) {
        assertTrue(logger, status, tips, (Object[]) null);
    }

    /**
     * 断言状态为true
     */
    public static void assertTrue(BiConsumer<String, Object[]> logger, Boolean status, String tips, Object... objs) {
        if (!Objects.equals(status, true)) {
            logger(logger, tips, objs);
        }
    }

    /**
     * 异常信息统一格式打印
     */
    public static void exception(String title, String message, String errorMessage, StackTraceElement throwPosition, StackTraceElement[] stackTraces) {
        String stackTrace = Arrays.stream(stackTraces).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
        String tips = "【{}】统一异常处理：[提示信息message]={} \n [错误信息error-message]={} \n [异常抛出位置throw-position]={}";
        if (CheckUtil.nonBlank(stackTrace)) {
            tips += "\n==================原追踪链路stack-trace==================\n" +
                    stackTrace +
                    "\n=======================================================\n";
        }
        logger(log::error, tips, title, message, errorMessage, throwPosition, stackTrace);
    }

    /**
     * 打印日志
     */
    private static void logger(BiConsumer<String, Object[]> logger, String tips, Object... objs) {
        ExceptionHandler.requireNotNull(log, CodeEnum.PARAM_ERROR, "LOG方法引用不能为空");
        logger.accept(tips, objs);
    }

    /**
     * 格式化tips
     */
    public static String format(String title, String tips) {
        return String.format(MSG_FORMAT, title, tips);
    }
}
