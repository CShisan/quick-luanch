package com.quick.domain.dao;

import com.quick.domain.entity.RolePermission;

import java.util.List;

/**
 * @author CShisan
 */
public interface RolePermissionDao {

    /**
     * 保存
     *
     * @param rolePermission rolePermission
     * @return row
     */
    int save(RolePermission rolePermission);

    /**
     * 批量移除角色关联的权限
     *
     * @param roleId        roleId
     * @param permissionIds permissionIds
     * @return row
     */
    int deleteBatch(Long roleId, List<Long> permissionIds);

    /**
     * 根据roleId查询list
     *
     * @param roleId roleId
     * @return list
     */
    List<RolePermission> listByRoleId(Long roleId);

    /**
     * 根据roleIds查询list
     *
     * @param roleIds roleIds
     * @return list
     */
    List<RolePermission> listByRoleIds(List<Long> roleIds);

    /**
     * 是否存在关联
     *
     * @param rolePermission rolePermission
     * @return 0否 1是
     */
    boolean existRelation(RolePermission rolePermission);

    /**
     * 查询全部
     *
     * @return list
     */
    List<RolePermission> all();

    /**
     * 批量保存
     *
     * @param entityList entityList
     * @return row
     */
    int saveBatch(List<RolePermission> entityList);
}
