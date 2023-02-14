package com.quick.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
public class BeanUtil {
    /**
     * bean转换(用于 目标对象 有源对象没有的字段 存在值的情况)
     *
     * @param source 源对象
     * @param target 目标对象
     * @return 目标对象
     */
    public static <S, T> T convert(S source, T target) {
        return Optional.ofNullable(source).map(item -> {
            BeanUtils.copyProperties(source, target);
            return target;
        }).orElse(null);
    }

    /**
     * bean转换
     *
     * @see BeanUtil#convert(Object, Supplier, ConvertCallBack)
     */
    public static <S, T> T convert(S source, Supplier<T> supplier) {
        return convert(source, supplier, null);
    }

    /**
     * bean转换
     *
     * @param source   源对象
     * @param supplier 函数式接口
     * @param callBack 回调接口
     * @return 目标对象
     */
    public static <S, T> T convert(S source, Supplier<T> supplier, ConvertCallBack<S, T> callBack) {
        if (CheckUtil.anyNull(source, supplier)) {
            return null;
        }

        // 复制bean属性
        T target = supplier.get();
        BeanUtils.copyProperties(source, target);

        // 回调不为空则执行回调方法
        if (CheckUtil.nonNull(callBack)) {
            callBack.callBack(source, target);
        }

        return target;
    }

    /**
     * list转换
     *
     * @see BeanUtil#listConvert(List, Supplier, ConvertCallBack)
     */
    public static <S, T> List<T> listConvert(List<S> sources, Supplier<T> supplier) {
        return listConvert(sources, supplier, null);
    }

    /**
     * list转换
     *
     * @param sources  源列表
     * @param supplier 函数式接口
     * @param callBack 回调接口
     * @return 目标列表
     */
    public static <S, T> List<T> listConvert(List<S> sources, Supplier<T> supplier, ConvertCallBack<S, T> callBack) {
        if (CheckUtil.isEmpty(sources) || CheckUtil.isNull(supplier)) {
            return new ArrayList<>();
        }
        return sources.stream()
                .map(source -> convert(source, supplier, callBack))
                .collect(Collectors.toList());
    }

    /**
     * 回调接口
     *
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     */
    @FunctionalInterface
    public interface ConvertCallBack<S, T> {
        /**
         * 回调方法
         *
         * @param s source
         * @param t target
         */
        void callBack(S s, T t);
    }
}
