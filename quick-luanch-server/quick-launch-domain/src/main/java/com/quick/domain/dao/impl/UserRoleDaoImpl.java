package com.quick.domain.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.domain.dao.UserRoleDao;
import com.quick.domain.entity.UserRole;
import com.quick.domain.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yuanbai
 */
@Component
public class UserRoleDaoImpl implements UserRoleDao {
    private final UserRoleMapper mapper;

    @Autowired
    public UserRoleDaoImpl(UserRoleMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据uid查询list
     *
     * @param uid uid
     * @return list
     */
    @Override
    public List<UserRole> listByUid(Long uid) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRole::getUid, uid);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<UserRole> listByRoleId(Long roleId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRole::getRoleId, roleId);
        return mapper.selectList(wrapper);
    }

    /**
     * 保存
     *
     * @param userRole userRole
     * @return row
     */
    @Override
    public int save(UserRole userRole) {
        return mapper.insert(userRole);
    }

    /**
     * 移除角色
     *
     * @param uid     uid
     * @param roleIds roleIds
     * @return row
     */
    @Override
    public int deleteBatch(Long uid, List<Long> roleIds) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRole::getUid, uid);
        wrapper.lambda().in(UserRole::getRoleId, roleIds);
        return mapper.delete(wrapper);
    }

    /**
     * 是否存在关联
     *
     * @param userRole userRole
     * @return 0否 1是
     */
    @Override
    public boolean existRelation(UserRole userRole) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();

        Long uid = userRole.getUid();
        wrapper.lambda().eq(ObjectUtils.isNotNull(uid), UserRole::getUid, uid);

        Long roleId = userRole.getRoleId();
        wrapper.lambda().eq(ObjectUtils.isNotNull(roleId), UserRole::getRoleId, roleId);

        wrapper.last("LIMIT 1");
        return mapper.exists(wrapper);
    }
}
