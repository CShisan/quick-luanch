package com.quick.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.utils.CheckUtil;

import java.util.Arrays;

/**
 * @author CShisan
 */
public class BasePage {
    private static final int DEFAULT_CURRENT = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int[] SIZES = new int[]{1, 5, 10, 15, 20, 25, 30};

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 获取当前页,最小值为1
     *
     * @return 当前页
     */
    public int getCurrent() {
        if (CheckUtil.isNull(current)) {
            return DEFAULT_CURRENT;
        }
        return Math.max(current, DEFAULT_CURRENT);
    }

    /**
     * 获取每页大小,若值不在SIZES中则直接设置大小为10
     *
     * @return 每页大小
     */
    public int getSize() {
        return Arrays.stream(SIZES).anyMatch(x -> x == size) ? size : DEFAULT_SIZE;
    }

    public <T> IPage<T> empty() {
        return new Page<>(DEFAULT_CURRENT, DEFAULT_SIZE);
    }

    public <T> IPage<T> page() {
        return new Page<>(getCurrent(), getSize());
    }

}
