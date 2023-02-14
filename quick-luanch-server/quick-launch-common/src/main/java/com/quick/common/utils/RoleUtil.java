package com.quick.common.utils;

import com.quick.common.enums.RedisKeyEnum;
import com.quick.domain.dao.PermissionDao;
import com.quick.domain.dao.RoleDao;
import com.quick.domain.dao.RolePermissionDao;
import com.quick.domain.entity.Role;
import com.quick.domain.entity.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Component
public class RoleUtil {
    private final RedisUtil redisUtil;
    private final RoleDao roleDao;
    private final RolePermissionDao rolePermissionDao;
    private final PermissionDao permissionDao;

    @Autowired
    public RoleUtil(RedisUtil redisUtil, RoleDao roleDao, RolePermissionDao rolePermissionDao, PermissionDao permissionDao) {
        this.redisUtil = redisUtil;
        this.roleDao = roleDao;
        this.rolePermissionDao = rolePermissionDao;
        this.permissionDao = permissionDao;
    }

    /**
     * 根据ids获取
     */
    public List<RelationUtil.Relation> relationBy(List<Long> ids) {
        Map<Long, RelationUtil.Relation> relation = this.relation(RelationUtil::idInherit);
        return ids.stream().map(relation::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 根据ids获取
     */
    public List<RelationUtil.Relation> bindRelationBy(List<Long> ids) {
        Map<Long, RelationUtil.Relation> relation = this.bindRelation(RelationUtil::idBind);
        return ids.stream().map(relation::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取角色映射关系
     */
    public <T> Map<T, RelationUtil.Relation> relation(Function<List<RelationUtil.Relation>, Map<T, RelationUtil.Relation>> function) {
        String key = RedisKeyEnum.USER_AUTH_RELATION_ROLES.getKey();
        return CacheUtil.cacheMap(key, redisUtil, () -> {
            // 查询角色信息
            List<Role> all = roleDao.all();

            // 构造映射关系
            List<RelationUtil.Relation> relations = all.stream().map(item ->
                    RelationUtil.Relation.builder().id(item.getRoleId()).key(item.getRoleKey()).build()
            ).collect(Collectors.toList());
            return function.apply(relations);
        });
    }

    /**
     * 获取角色与权限的映射关系
     */
    public <T> Map<T, RelationUtil.Relation> bindRelation(
            RelationUtil.ThreeFunction<List<RelationUtil.Relation>, Map<Long, List<Long>>, List<RelationUtil.Relation>, Map<T, RelationUtil.Relation>> function
    ) {
        String key = RedisKeyEnum.USER_AUTH_RELATION_ROLE_PERMISSIONS.getKey();
        return CacheUtil.cacheMap(key, redisUtil, () -> {
            // 查询权限信息
            List<RelationUtil.Relation> permissions = permissionDao.all().stream().map(item ->
                    RelationUtil.Relation.builder().id(item.getPermissionId()).key(item.getPermissionKey())
                            .leaf(item.getLeafFlag()).name(item.getPermissionName()).build()
            ).collect(Collectors.toList());

            // 查询角色与权限绑定关系信息
            Map<Long, List<Long>> bindMap = rolePermissionDao.all().stream().collect(Collectors.groupingBy(
                    RolePermission::getRoleId, Collectors.mapping(RolePermission::getPermissionId, Collectors.toList())
            ));

            // 查询角色信息
            List<RelationUtil.Relation> resources = roleDao.all().stream().map(item ->
                    RelationUtil.Relation.builder().id(item.getRoleId()).key(item.getRoleKey()).name(item.getRoleName()).build()
            ).collect(Collectors.toList());

            return function.apply(resources, bindMap, permissions);
        });
    }
}
