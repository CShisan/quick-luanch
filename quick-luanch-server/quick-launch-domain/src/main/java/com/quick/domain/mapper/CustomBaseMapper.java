package com.quick.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yuanbai
 */
public interface CustomBaseMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入 仅适用于mysql
     *
     * @param list 实体列表
     * @return 影响行数
     */
    int insertBatchSomeColumn(@Param("list") List<?> list);
}
