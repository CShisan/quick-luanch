package com.quick.domain.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.domain.dao.RoleDao;
import com.quick.domain.entity.Role;
import com.quick.domain.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author CShisan
 */
@Component
public class RoleDaoImpl implements RoleDao {
    private final RoleMapper mapper;

    @Autowired
    public RoleDaoImpl(RoleMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据roleId查询
     *
     * @param roleId roleId
     * @return role
     */
    @Override
    public Role getOneById(Long roleId) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Role::getRoleId, roleId);
        return mapper.selectOne(wrapper);
    }

    /**
     * 根据roleIds查询list
     *
     * @param roleIds roleIds
     * @return list
     */
    @Override
    public List<Role> listByRoleIds(List<Long> roleIds) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Role::getRoleId, roleIds);
        return mapper.selectList(wrapper);
    }

    /**
     * 分页
     *
     * @param entity entity
     * @param page   page
     * @return page
     */
    @Override
    public IPage<Role> page(Role entity, IPage<Role> page) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();

        String roleName = entity.getRoleName();
        wrapper.lambda().likeLeft(ObjectUtils.isNotEmpty(roleName), Role::getRoleName, roleName);

        return mapper.selectPage(page, wrapper);
    }

    /**
     * 根据角色ID删除
     *
     * @param roleId roleId
     * @return row
     */
    @Override
    public int deleteById(Long roleId) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Role::getRoleId, roleId);
        return mapper.delete(wrapper);
    }

    /**
     * 根据roleId更新
     *
     * @param role role
     * @return row
     */
    @Override
    public int updateById(Role role) {
        UpdateWrapper<Role> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(Role::getRoleId, role.getRoleId());

        String roleKey = role.getRoleKey();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(roleKey), Role::getRoleKey, roleKey);

        String roleName = role.getRoleName();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(roleName), Role::getRoleName, roleName);

        return mapper.update(new Role(), wrapper);
    }

    /**
     * 新增
     *
     * @param role role
     * @return row
     */
    @Override
    public int save(Role role) {
        return mapper.insert(role);
    }

    /**
     * 查询全部
     *
     * @return list
     */
    @Override
    public List<Role> all() {
        return mapper.selectList(new QueryWrapper<>());
    }
}
