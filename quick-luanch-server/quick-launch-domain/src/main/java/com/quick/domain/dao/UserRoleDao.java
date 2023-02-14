package com.quick.domain.dao;

import com.quick.domain.entity.UserRole;

import java.util.List;

/**
 * @author yuanbai
 */
public interface UserRoleDao {
    /**
     * 根据uid查询list
     *
     * @param uid uid
     * @return list
     */
    List<UserRole> listByUid(Long uid);

    /**
     * 根据roleId查询list
     *
     * @param roleId roleId
     * @return list
     */
    List<UserRole> listByRoleId(Long roleId);

    /**
     * 保存
     *
     * @param userRole uid
     * @return row
     */
    int save(UserRole userRole);

    /**
     * 批量移除用户关联的角色
     *
     * @param uid     uid
     * @param roleIds roleIds
     * @return row
     */
    int deleteBatch(Long uid, List<Long> roleIds);

    /**
     * 是否存在关联
     *
     * @param userRole userRole
     * @return 0否 1是
     */
    boolean existRelation(UserRole userRole);
}
