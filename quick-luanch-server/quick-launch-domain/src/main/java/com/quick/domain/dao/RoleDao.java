package com.quick.domain.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.domain.entity.Role;

import java.util.List;

/**
 * @author CShisan
 */
public interface RoleDao {
    /**
     * 根据roleId查询
     *
     * @param roleId roleId
     * @return role
     */
    Role getOneById(Long roleId);

    /**
     * 根据roleIds查询list
     *
     * @param roleIds roleIds
     * @return list
     */
    List<Role> listByRoleIds(List<Long> roleIds);

    /**
     * 分页
     *
     * @param entity entity
     * @param page   page
     * @return page
     */
    IPage<Role> page(Role entity, IPage<Role> page);

    /**
     * 根据角色ID删除
     *
     * @param roleId roleId
     * @return row
     */
    int deleteById(Long roleId);

    /**
     * 根据roleId更新
     *
     * @param role role
     * @return row
     */
    int updateById(Role role);

    /**
     * 新增
     *
     * @param role role
     * @return row
     */
    int save(Role role);

    /**
     * 查询全部
     *
     * @return list
     */
    List<Role> all();
}
