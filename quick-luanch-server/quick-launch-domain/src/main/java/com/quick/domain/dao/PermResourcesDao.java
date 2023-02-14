package com.quick.domain.dao;

import com.quick.domain.entity.PermResources;

import java.util.List;

/**
 * @author CShisan
 */
public interface PermResourcesDao {
    /**
     * 根据权限ID查询
     *
     * @param permissionId permissionId
     * @return entity
     */
    List<PermResources> listByPermissionId(Long permissionId);

    /**
     * 根据资源ID查询
     *
     * @param resourceId resourceId
     * @return entity
     */
    List<PermResources> listByResourceId(Long resourceId);

    /**
     * 根据权限IDS查询
     *
     * @param permissionIds permissionIds
     * @return entity
     */
    List<PermResources> listByPermissionIds(List<Long> permissionIds);

    /**
     * 根据资源IDS查询
     *
     * @param resourceIds resourceIds
     * @return entity
     */
    List<PermResources> listByResourceIds(List<Long> resourceIds);

    /**
     * 根据资源ID查询是否存在绑定关系
     *
     * @param resourceId resourceId
     * @return status
     */
    boolean existRelation(Long resourceId);

    /**
     * 新增
     *
     * @param entity entity
     * @return row
     */
    int save(PermResources entity);

    /**
     * 批量新增
     *
     * @param entityList entityList
     * @return row
     */
    int saveBatch(List<PermResources> entityList);

    /**
     * 查询所有
     *
     * @return list
     */
    List<PermResources> all();

    /**
     * 批量删除
     *
     * @param permissionId permissionId
     * @param reduceList   reduceList
     * @return row
     */
    int deleteBatch(Long permissionId, List<Long> reduceList);
}
