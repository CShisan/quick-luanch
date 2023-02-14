package com.quick.common.utils;

import com.quick.common.enums.RedisKeyEnum;
import com.quick.domain.dao.PermResourcesDao;
import com.quick.domain.dao.PermissionDao;
import com.quick.domain.dao.ResourcesDao;
import com.quick.domain.entity.PermResources;
import com.quick.domain.entity.Resources;
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
public class ResourceUtil {
    private final RedisUtil redisUtil;
    private final ResourcesDao resourcesDao;
    private final PermResourcesDao permResourcesDao;
    private final PermissionDao permissionDao;

    @Autowired
    public ResourceUtil(RedisUtil redisUtil, ResourcesDao resourcesDao, PermResourcesDao permResourcesDao, PermissionDao permissionDao) {
        this.redisUtil = redisUtil;
        this.resourcesDao = resourcesDao;
        this.permResourcesDao = permResourcesDao;
        this.permissionDao = permissionDao;
    }

    /**
     * 根据ids获取
     */
    public List<RelationUtil.Relation> listBy(List<Long> ids) {
        Map<Long, RelationUtil.Relation> relation = this.relation(RelationUtil::idInherit);
        return ids.stream().map(relation::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 根据ids和类型获取
     */
    public List<RelationUtil.Relation> listBy(List<Long> ids, ResourceTypeEnum type) {
        Map<Long, RelationUtil.Relation> relation = this.relation(RelationUtil::idInherit);
        return ids.stream().map(relation::get)
                .filter(Objects::nonNull)
                .filter(item -> Objects.equals(type.getCode(), item.getType()))
                .collect(Collectors.toList());
    }

    /**
     * 获取资源映射关系
     */
    public <T> Map<T, RelationUtil.Relation> relation(Function<List<RelationUtil.Relation>, Map<T, RelationUtil.Relation>> function) {
        String key = RedisKeyEnum.USER_AUTH_RELATION_RESOURCES.getKey();
        return CacheUtil.cacheMap(key, redisUtil, () -> {
            List<Resources> all = resourcesDao.all();
            List<RelationUtil.Relation> relations = all.stream().map(item -> {
                Integer type = Optional.ofNullable(item.getType()).map(ResourceTypeEnum::getCode).orElse(null);
                // 获取继承的资源
                return RelationUtil.Relation.builder().id(item.getResourceId())
                        .key(item.getPath()).name(item.getDescription()).type(type)
                        .relationIds(new ArrayList<>(Collections.singletonList(item.getPid()))).build();
            }).collect(Collectors.toList());
            return function.apply(relations);
        });
    }

    /**
     * 获取资源绑定权限的映射关系
     */
    public <T> Map<T, RelationUtil.Relation> bindRelation(
            RelationUtil.ThreeFunction<List<RelationUtil.Relation>, Map<Long, List<Long>>, List<RelationUtil.Relation>, Map<T, RelationUtil.Relation>> function
    ) {
        return this.bindRelation(function, null);
    }

    /**
     * 获取资源绑定权限的映射关系
     */
    public <T> Map<T, RelationUtil.Relation> bindRelation(
            RelationUtil.ThreeFunction<List<RelationUtil.Relation>, Map<Long, List<Long>>, List<RelationUtil.Relation>, Map<T, RelationUtil.Relation>> function,
            ResourceTypeEnum type
    ) {
        String key = RedisKeyEnum.USER_AUTH_RELATION_RESOURCE_PERMISSIONS.getKey();
        return CacheUtil.cacheMap(key, redisUtil, () -> {
            // 查询权限信息
            List<RelationUtil.Relation> secondaryList = permissionDao.all().stream().map(item ->
                    RelationUtil.Relation.builder().id(item.getPermissionId()).key(item.getPermissionKey())
                            .name(item.getPermissionName()).leaf(item.getLeafFlag()).build()
            ).collect(Collectors.toList());

            // 查询资源与权限绑定关系信息
            Map<Long, List<Long>> bindMap = permResourcesDao.all().stream().collect(Collectors.groupingBy(
                    PermResources::getResourceId, Collectors.mapping(PermResources::getPermissionId, Collectors.toList())
            ));

            // 查询资源信息
            List<RelationUtil.Relation> primaryList = resourcesDao.all().stream().filter(resource ->
                    CheckUtil.isNull(type) || Objects.equals(type, resource.getType())
            ).map(item -> {
                Integer typeCode = Optional.ofNullable(item.getType()).map(ResourceTypeEnum::getCode).orElse(null);
                return RelationUtil.Relation.builder().id(item.getResourceId()).key(item.getPath())
                        .name(item.getDescription()).type(typeCode).build();
            }).collect(Collectors.toList());

            // 根据绑定关系构造inheritances
            return function.apply(primaryList, bindMap, secondaryList);
        });
    }
}
