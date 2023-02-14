package com.quick.common.utils;

import com.quick.common.enums.RedisKeyEnum;
import com.quick.domain.dao.PermResourcesDao;
import com.quick.domain.dao.PermissionDao;
import com.quick.domain.dao.ResourcesDao;
import com.quick.domain.entity.PermResources;
import com.quick.domain.entity.Permission;
import com.quick.domain.enums.ResourceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Component
public class PermissionUtil {
    private final RedisUtil redisUtil;
    private final PermissionDao permissionDao;
    private final PermResourcesDao permResourcesDao;
    private final ResourcesDao resourcesDao;

    @Autowired
    public PermissionUtil(RedisUtil redisUtil, PermissionDao permissionDao, PermResourcesDao permResourcesDao, ResourcesDao resourcesDao) {
        this.redisUtil = redisUtil;
        this.permissionDao = permissionDao;
        this.permResourcesDao = permResourcesDao;
        this.resourcesDao = resourcesDao;
    }

    /**
     * 获取继承关系中的所有id
     */
    public List<Long> fullIds(List<Long> ids) {
        Map<Long, RelationUtil.Relation> relation = relation(RelationUtil::idInherit);
        Map<Long, RelationUtil.Relation> reverse = RelationUtil.reverse(relation, RelationUtil.Relation::getId);
        return RelationUtil.fullNodeArgs(ids, reverse, RelationUtil.Relation::getId);
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
     * 验证权限
     *
     * @param owns     用户拥有的权限
     * @param required 要求的子权限
     * @return status
     */
    public boolean access(List<String> owns, List<String> required) {
        return RelationUtil.hasKey(owns, required, this.relation(RelationUtil::keyInherit));
    }

    /**
     * 获取权限映射关系
     */
    public <T> Map<T, RelationUtil.Relation> relation(Function<List<RelationUtil.Relation>, Map<T, RelationUtil.Relation>> function) {
        String key = RedisKeyEnum.USER_AUTH_RELATION_PERMISSIONS.getKey();
        return CacheUtil.cacheMap(key, redisUtil, () -> {
            List<Permission> all = permissionDao.all();
            List<RelationUtil.Relation> relations = all.stream().map(item ->
                    RelationUtil.Relation.builder().id(item.getPermissionId())
                            .key(item.getPermissionKey()).name(item.getPermissionName()).leaf(item.getLeafFlag())
                            .relationIds(new ArrayList<>(Collections.singletonList(item.getPid()))).build()
            ).collect(Collectors.toList());
            return function.apply(relations);
        });
    }

    /**
     * 获取权限与资源的映射关系
     */
    public <T> Map<T, RelationUtil.Relation> bindRelation(
            RelationUtil.ThreeFunction<List<RelationUtil.Relation>, Map<Long, List<Long>>, List<RelationUtil.Relation>, Map<T, RelationUtil.Relation>> function
    ) {
        String key = RedisKeyEnum.USER_AUTH_RELATION_PERMISSION_RESOURCES.getKey();
        return CacheUtil.cacheMap(key, redisUtil, () -> {
            // 查询资源信息
            List<RelationUtil.Relation> secondaryList = resourcesDao.all().stream().map(item -> {
                Integer type = Optional.ofNullable(item.getType()).map(ResourceTypeEnum::getCode).orElse(null);
                return RelationUtil.Relation.builder().id(item.getResourceId()).key(item.getPath())
                        .name(item.getDescription()).type(type).build();
            }).collect(Collectors.toList());

            // 查询权限与资源绑定关系信息
            Map<Long, List<Long>> bindMap = permResourcesDao.all().stream().collect(Collectors.groupingBy(
                    PermResources::getPermissionId, Collectors.mapping(PermResources::getResourceId, Collectors.toList())
            ));

            // 查询权限信息
            List<RelationUtil.Relation> primaryList = permissionDao.all().stream().map(item ->
                    RelationUtil.Relation.builder().id(item.getPermissionId())
                            .key(item.getPermissionKey()).name(item.getPermissionName())
                            .leaf(item.getLeafFlag()).build()
            ).collect(Collectors.toList());

            return function.apply(primaryList, bindMap, secondaryList);
        });
    }
}
