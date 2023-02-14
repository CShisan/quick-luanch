package com.quick.domain.dao;

import com.quick.domain.entity.Permission;

import java.util.List;

/**
 * @author CShisan
 */
public interface PermissionDao {
    /**
     * 根据permissionIds查询list
     *
     * @param permissionIds permissionIds
     * @return list
     */
    List<Permission> listByPermissionIds(List<Long> permissionIds);

    /**
     * 根据pid查询list
     *
     * @param pid pid
     * @return list
     */
    List<Permission> listByPid(Long pid);

    /**
     * 根据权限ID查询
     *
     * @param permissionId permissionId
     * @return entity
     */
    Permission getOneById(Long permissionId);


    /**
     * 根据权限ID删除
     *
     * @param permissionId 权限ID
     * @return row
     */
    int deleteByPermissionId(Long permissionId);

    /**
     * 根据权限ID更新
     *
     * @param permission permission
     * @return row
     */
    int updateById(Permission permission);

    /**
     * 新增
     *
     * @param permission permission
     * @return row
     */
    int save(Permission permission);

    /**
     * 获取全部权限
     *
     * @return list
     */
    List<Permission> all();
}
