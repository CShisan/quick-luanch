package com.quick.common.exception;

import com.quick.common.enums.CodeEnum;
import com.quick.common.utils.CheckUtil;

import java.util.Objects;

/**
 * @author CShisan
 */
public class ExceptionHandler {

    /**
     * 断言操作数据库单个成功
     *
     * @see ExceptionHandler#operateDbSingleOk(int, CodeEnum)
     */
    public static void operateDbSingleOk(int row) {
        operateDbSingleOk(row, CodeEnum.NO);
    }

    /**
     * 断言操作数据库单个成功
     *
     * @see ExceptionHandler#operateDbSingleOk(int, CodeEnum, String, boolean)
     */
    public static void operateDbSingleOk(int row, CodeEnum codeEnum) {
        operateDbSingleOk(row, codeEnum, codeEnum.getMessage(), false);
    }

    /**
     * 断言操作数据库单个成功
     *
     * @see ExceptionHandler#operateDbSingleOk(int, CodeEnum, String, boolean)
     */
    public static void operateDbSingleOk(int row, String msg) {
        operateDbSingleOk(row, CodeEnum.NO, msg, false);
    }

    /**
     * 断言操作数据库单个成功
     *
     * @see ExceptionHandler#operateDbSingleOk(int, CodeEnum, String, boolean)
     */
    public static void operateDbSingleOk(int row, String msg, boolean logicalDelete) {
        operateDbSingleOk(row, CodeEnum.NO, msg, logicalDelete);
    }

    /**
     * 断言操作数据库单个成功
     */
    public static void operateDbSingleOk(int row, CodeEnum codeEnum, String msg, boolean logicalDelete) {
        // 兼容mybatis-plus的逻辑删除影响行为0的问题
        boolean allow = row == 1 || (logicalDelete && row == 0);
        assertTrue(allow, codeEnum, msg);
    }

    /**
     * 断言操作数据库多个成功
     *
     * @see ExceptionHandler#operateDbMultipleOk(int, CodeEnum)
     */
    public static void operateDbMultipleOk(int row) {
        operateDbMultipleOk(row, CodeEnum.NO);
    }

    /**
     * 断言操作数据库多个成功
     *
     * @see ExceptionHandler#operateDbMultipleOk(int, CodeEnum, String)
     */
    public static void operateDbMultipleOk(int row, CodeEnum codeEnum) {
        operateDbMultipleOk(row, codeEnum, codeEnum.getMessage());
    }

    /**
     * 断言操作数据库多个成功
     *
     * @see ExceptionHandler#operateDbMultipleOk(int, CodeEnum, String)
     */
    public static void operateDbMultipleOk(int row, String msg) {
        operateDbMultipleOk(row, CodeEnum.NO, msg);
    }

    /**
     * 断言操作数据库多个成功
     */
    public static void operateDbMultipleOk(int row, CodeEnum codeEnum, String msg) {
        assertTrue(row > 0, codeEnum, msg);
    }

    /**
     * 判断不为空,否则抛出异常
     *
     * @see ExceptionHandler#requireNotNull(Object, CodeEnum, String)
     */
    public static void requireNotNull(Object obj) {
        requireNotNull(obj, CodeEnum.SERVICE_DATA_NULL, null);
    }

    /**
     * 判断不为空,否则抛出异常
     *
     * @see ExceptionHandler#requireNotNull(Object, CodeEnum, String)
     */
    public static void requireNotNull(Object obj, CodeEnum codeEnum) {
        requireNotNull(obj, codeEnum, null);
    }

    /**
     * 判断不为空,否则抛出异常
     */
    public static void requireNotNull(Object obj, CodeEnum codeEnum, String msg) {
        assertTrue(CheckUtil.nonNull(obj), codeEnum, msg);
    }

    /**
     * 判断obj不为empty
     *
     * @see ExceptionHandler#requireNotEmpty(Object, CodeEnum, String)
     */
    public static void requireNotEmpty(Object obj) {
        requireNotEmpty(obj, CodeEnum.SERVICE_DATA_NULL, null);
    }

    /**
     * 判断obj不为empty
     *
     * @see ExceptionHandler#requireNotEmpty(Object, CodeEnum, String)
     */
    public static void requireNotEmpty(Object obj, CodeEnum codeEnum) {
        requireNotEmpty(obj, codeEnum, null);
    }

    /**
     * 判断obj不为empty
     */
    public static void requireNotEmpty(Object obj, CodeEnum codeEnum, String msg) {
        assertTrue(CheckUtil.nonEmpty(obj), codeEnum, msg);
    }

    /**
     * 判断obj不为空字符串
     *
     * @see ExceptionHandler#requireNotBlank(Object, CodeEnum, String)
     */
    public static void requireNotBlank(Object obj) {
        requireNotBlank(obj, CodeEnum.SERVICE_DATA_NULL, null);
    }

    /**
     * 判断obj不为空字符串
     *
     * @see ExceptionHandler#requireNotBlank(Object, CodeEnum, String)
     */
    public static void requireNotBlank(Object obj, CodeEnum codeEnum) {
        requireNotBlank(obj, codeEnum, null);
    }

    /**
     * 判断obj不为空字符串
     */
    public static void requireNotBlank(Object obj, CodeEnum codeEnum, String msg) {
        assertTrue(CheckUtil.nonBlank(obj), codeEnum, msg);
    }

    /**
     * 断言状态为true
     *
     * @see ExceptionHandler#assertTrue(Boolean, CodeEnum, String)
     */
    public static void assertTrue(Boolean status) {
        assertTrue(status, CodeEnum.SERVICE_DATA_ERROR, null);
    }

    /**
     * 断言状态为true
     *
     * @see ExceptionHandler#assertTrue(Boolean, CodeEnum, String)
     */
    public static void assertTrue(Boolean status, CodeEnum codeEnum) {
        assertTrue(status, codeEnum, null);
    }

    /**
     * 断言状态为true
     */
    public static void assertTrue(Boolean status, CodeEnum codeEnum, String msg) {
        if (!Objects.equals(status, true)) {
            throw new ServiceException(codeEnum, CheckUtil.isBlank(msg) ? codeEnum.getMessage() : msg);
        }
    }
}
