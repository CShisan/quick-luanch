package com.quick.domain.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.domain.entity.Resources;
import com.quick.domain.enums.ResourceTypeEnum;

import java.util.List;

/**
 * @author CShisan
 */
public interface ResourcesDao {

    /**
     * 根据resourceIds查询list
     *
     * @param resourceIds resourceIds
     * @return list
     */
    List<Resources> listByResourceIds(List<Long> resourceIds);

    /**
     * 分页
     *
     * @param entity entity
     * @param page   page
     * @return page
     */
    IPage<Resources> page(Resources entity, IPage<Resources> page);

    /**
     * 根据资源ID查询
     *
     * @param resourceId resourceId
     * @return entity
     */
    Resources getOneById(Long resourceId);

    /**
     * 新增
     *
     * @param resources resources
     * @return row
     */
    int save(Resources resources);

    /**
     * 根据ID更新
     *
     * @param resources resources
     * @return row
     */
    int updateById(Resources resources);

    /**
     * 根据ID删除
     *
     * @param resourceId resourceId
     * @return row
     */
    int deleteById(Long resourceId);

    /**
     * 根据pid查询列表
     *
     * @param pid pid
     * @return list
     */
    List<Resources> listByPid(Long pid);

    /**
     * 查询全部
     *
     * @return list
     */
    List<Resources> listAll();

    /**
     * 查询对应类型的全部记录
     *
     * @param type type
     * @return list
     */
    List<Resources> listByType(ResourceTypeEnum type);

    /**
     * 查询全部
     *
     * @return list
     */
    List<Resources> all();
}
