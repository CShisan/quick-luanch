package com.quick.domain.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.domain.dao.RolePermissionDao;
import com.quick.domain.entity.RolePermission;
import com.quick.domain.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author CShisan
 */
@Component
public class RolePermissionDaoImpl implements RolePermissionDao {
    private final RolePermissionMapper mapper;

    @Autowired
    public RolePermissionDaoImpl(RolePermissionMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 保存
     *
     * @param rolePermission rolePermission
     * @return row
     */
    @Override
    public int save(RolePermission rolePermission) {
        return mapper.insert(rolePermission);
    }

    /**
     * 批量移除角色关联的权限
     *
     * @param roleId        roleId
     * @param permissionIds permissionIds
     * @return row
     */
    @Override
    public int deleteBatch(Long roleId, List<Long> permissionIds) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        wrapper.lambda().in(RolePermission::getPermissionId, permissionIds);
        return mapper.delete(wrapper);
    }

    /**
     * 根据roleId查询list
     *
     * @param roleId roleId
     * @return list
     */
    @Override
    public List<RolePermission> listByRoleId(Long roleId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        return mapper.selectList(wrapper);
    }

    /**
     * 根据roleIds查询list
     *
     * @param roleIds roleIds
     * @return list
     */
    @Override
    public List<RolePermission> listByRoleIds(List<Long> roleIds) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(RolePermission::getRoleId, roleIds);
        return mapper.selectList(wrapper);
    }

    /**
     * 是否存在关联
     *
     * @param rolePermission rolePermission
     * @return 0否 1是
     */
    @Override
    public boolean existRelation(RolePermission rolePermission) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();

        Long roleId = rolePermission.getRoleId();
        wrapper.lambda().eq(ObjectUtils.isNotNull(roleId), RolePermission::getRoleId, roleId);

        Long permissionId = rolePermission.getPermissionId();
        wrapper.lambda().eq(ObjectUtils.isNotNull(permissionId), RolePermission::getPermissionId, permissionId);

        wrapper.last("LIMIT 1");
        return mapper.exists(wrapper);
    }

    /**
     * 查询全部
     *
     * @return list
     */
    @Override
    public List<RolePermission> all() {
        return mapper.selectList(new QueryWrapper<>());
    }

    /**
     * 批量保存
     *
     * @param entityList entityList
     * @return row
     */
    @Override
    public int saveBatch(List<RolePermission> entityList) {
        return mapper.insertBatchSomeColumn(entityList);
    }
}
