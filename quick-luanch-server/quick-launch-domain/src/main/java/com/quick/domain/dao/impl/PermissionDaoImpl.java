package com.quick.domain.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.domain.dao.PermissionDao;
import com.quick.domain.entity.Permission;
import com.quick.domain.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author CShisan
 */
@Component
public class PermissionDaoImpl implements PermissionDao {
    private final PermissionMapper mapper;

    @Autowired
    public PermissionDaoImpl(PermissionMapper mapper) {
        this.mapper = mapper;
    }


    /**
     * 根据permissionIds查询list
     *
     * @param permissionIds permissionIds
     * @return list
     */
    @Override
    public List<Permission> listByPermissionIds(List<Long> permissionIds) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Permission::getPermissionId, permissionIds);
        wrapper.select("DISTINCT *");
        return mapper.selectList(wrapper);
    }

    /**
     * 根据pid查询list
     *
     * @param pid pid
     * @return list
     */
    @Override
    public List<Permission> listByPid(Long pid) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Permission::getPid, pid);
        return mapper.selectList(wrapper);
    }

    /**
     * 根据权限ID查询
     *
     * @param permissionId permissionId
     * @return entity
     */
    @Override
    public Permission getOneById(Long permissionId) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Permission::getPermissionId, permissionId);
        return mapper.selectOne(wrapper);
    }

    /**
     * 根据权限ID删除
     *
     * @param permissionId 权限ID
     * @return row
     */
    @Override
    public int deleteByPermissionId(Long permissionId) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Permission::getPermissionId, permissionId);
        return mapper.delete(wrapper);
    }

    /**
     * 根据权限ID更新
     *
     * @param permission permission
     * @return row
     */
    @Override
    public int updateById(Permission permission) {
        UpdateWrapper<Permission> wrapper = new UpdateWrapper<>();
        Long permissionId = permission.getPermissionId();
        wrapper.lambda().eq(Permission::getPermissionId, permissionId);

        String permissionKey = permission.getPermissionKey();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(permissionKey), Permission::getPermissionKey, permissionKey);

        String permissionName = permission.getPermissionName();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(permissionName), Permission::getPermissionName, permissionName);

        return mapper.update(new Permission(), wrapper);
    }


    /**
     * 新增
     *
     * @param permission permission
     * @return row
     */
    @Override
    public int save(Permission permission) {
        return mapper.insert(permission);
    }

    /**
     * 获取全部权限
     *
     * @return list
     */
    @Override
    public List<Permission> all() {
        return mapper.selectList(new QueryWrapper<>());
    }
}
