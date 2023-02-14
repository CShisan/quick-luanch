package com.quick.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
public class PageUtil {
    /**
     * 将类型为S的分页转换到类型为T的分页
     * 包含分页记录的转换
     *
     * @param source  source
     * @param records records
     * @return page
     */
    public static <S, T> IPage<T> convert(IPage<S> source, List<T> records) {
        ExceptionHandler.requireNotNull(source, CodeEnum.PARAM_ERROR, "分页不能为空");
        IPage<T> page = new Page<>(source.getCurrent(), source.getSize(), source.getTotal());
        page.setRecords(records);
        return page;
    }

    /**
     * 将类型为S的分页转换到类型为T的分页
     * 包含分页记录的转换
     * !!!仅能对 相同名称 的字段进行复制!!!
     *
     * @param source   source
     * @param supplier supplier
     * @return page
     */
    public static <S, T> IPage<T> convert(IPage<S> source, Supplier<T> supplier) {
        ExceptionHandler.requireNotNull(source, CodeEnum.PARAM_ERROR, "分页不能为空");
        List<T> result = source.getRecords().stream()
                .map(record -> BeanUtil.convert(record, supplier))
                .collect(Collectors.toList());
        return convert(source, result);
    }
}
