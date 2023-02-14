package com.quick.auth.security.datasource;

import com.quick.auth.entity.LoginEntity;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.*;
import com.quick.domain.dao.UserDao;
import com.quick.domain.dao.UserRoleDao;
import com.quick.domain.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Component
public class UserDetailsService {
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final RoleUtil roleUtil;
    private final PermissionUtil permissionUtil;

    @Autowired
    public UserDetailsService(UserDao userDao, UserRoleDao userRoleDao, RoleUtil roleUtil, PermissionUtil permissionUtil) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.roleUtil = roleUtil;
        this.permissionUtil = permissionUtil;
    }

    /**
     * 根据uid查询user
     *
     * @param uid uid
     */
    public UserDetails loadUserByUid(Long uid) {
        // 查询用户
        LoginEntity entity = BeanUtil.convert(userDao.getOneById(uid), LoginEntity::new);
        ExceptionHandler.requireNotNull(entity, CodeEnum.USER_AUTH_ERROR);

        // 查询拥有角色
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        ExceptionHandler.requireNotEmpty(roleIds, CodeEnum.SERVICE_DATA_ERROR, "用户角色异常");
        List<RelationUtil.Relation> roles = roleUtil.relationBy(roleIds);
        List<String> roleKeys = roles.stream().map(RelationUtil.Relation::getKey).collect(Collectors.toList());
        entity.setRoles(roleKeys);

        List<RelationUtil.Relation> rolePermissions = roleUtil.bindRelationBy(roleIds);
        if (CheckUtil.nonEmpty(rolePermissions)) {
            // 查询权限信息
            List<Long> permissionIds = rolePermissions.stream()
                    .map(RelationUtil.Relation::getRelationIds)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            // 获取全部权限id
            List<RelationUtil.Relation> permissions = permissionUtil.relationBy(permissionIds);
            Set<String> permissionKeys = permissions.stream().map(RelationUtil.Relation::getKey).collect(Collectors.toSet());
            ExceptionHandler.requireNotEmpty(permissionKeys, CodeEnum.SERVICE_DATA_ERROR, "用户权限异常");
            entity.setPermissions(permissionKeys);
        }
        return entity;
    }
}
