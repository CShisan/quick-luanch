package com.quick.platform.service;

import com.quick.common.aop.annotation.CacheDelete;
import com.quick.common.enums.CodeEnum;
import com.quick.common.enums.RedisKeyEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.*;
import com.quick.domain.dao.PermResourcesDao;
import com.quick.domain.dao.PermissionDao;
import com.quick.domain.dao.ResourcesDao;
import com.quick.domain.dao.RolePermissionDao;
import com.quick.domain.entity.PermResources;
import com.quick.domain.entity.Permission;
import com.quick.domain.entity.Resources;
import com.quick.domain.entity.RolePermission;
import com.quick.platform.dto.permission.*;
import com.quick.platform.vo.permission.DetailPermissionPfVO;
import com.quick.platform.vo.permission.TreePermissionPfVO;
import com.quick.platform.vo.resources.RelationResourcesPfVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Service
public class PermissionService {
    private final PermissionDao permissionDao;
    private final RolePermissionDao rolePermissionDao;
    private final ResourcesDao resourcesDao;
    private final PermResourcesDao permResourcesDao;
    private final PermissionUtil permissionUtil;
    private final RedisUtil redisUtil;

    @Autowired
    public PermissionService(PermissionDao permissionDao, RolePermissionDao rolePermissionDao, ResourcesDao resourcesDao, PermResourcesDao permResourcesDao, PermissionUtil permissionUtil, RedisUtil redisUtil) {
        this.permissionDao = permissionDao;
        this.rolePermissionDao = rolePermissionDao;
        this.resourcesDao = resourcesDao;
        this.permResourcesDao = permResourcesDao;
        this.permissionUtil = permissionUtil;
        this.redisUtil = redisUtil;
    }

    /**
     * 根据pid查询子权限树
     *
     * @param pid pid
     * @return list
     */
    public List<TreePermissionPfVO> treePermission(Long pid) {
        ExceptionHandler.requireNotNull(pid, CodeEnum.PARAM_ERROR, "父ID不能为空");
        Map<Long, RelationUtil.Relation> relation = permissionUtil.relation(RelationUtil::idInherit);
        Map<Long, RelationUtil.Relation> reverse = RelationUtil.reverse(relation, RelationUtil.Relation::getId);
        // 构造映射
        Map<Long, TreePermissionPfVO> relationMap = reverse.values().stream().collect(Collectors.toMap(RelationUtil.Relation::getId, item -> {
            TreePermissionPfVO node = new TreePermissionPfVO();
            node.setPermissionId(item.getId());
            node.setPermissionName(item.getName());
            node.setLeafFlag(item.getLeaf());
            node.setChildren(new ArrayList<>());
            return node;
        }));
        // 填充子节点
        for (RelationUtil.Relation value : reverse.values()) {
            List<TreePermissionPfVO> children = Optional.ofNullable(value.getRelationIds()).map(id ->
                    id.stream().map(relationMap::get).filter(Objects::nonNull).collect(Collectors.toList())
            ).orElse(new ArrayList<>());
            Collections.reverse(children);
            TreePermissionPfVO node = relationMap.get(value.getId());
            node.setChildren(children);
        }
        return Optional.ofNullable(relationMap.get(pid)).map(TreePermissionPfVO::getChildren).orElse(new ArrayList<>());
    }

    /**
     * 根据权限ID查询
     *
     * @param permissionId 权限ID
     * @return status
     */
    public DetailPermissionPfVO getOneById(Long permissionId) {
        ExceptionHandler.requireNotNull(permissionId, CodeEnum.PARAM_ERROR, "权限ID不能为空");
        Permission permission = permissionDao.getOneById(permissionId);
        return BeanUtil.convert(permission, DetailPermissionPfVO::new);
    }

    /**
     * 更新
     *
     * @param dto dto
     * @return status
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheDelete({RedisKeyEnum.USER_AUTH_RELATION_PERMISSIONS, RedisKeyEnum.USER_AUTH_RELATION_PERMISSION_RESOURCES})
    public Boolean updateById(UpdatePermissionPfDTO dto) {
        // 检查父节点启用状态
        Long pid = dto.getPid();
        Permission parent = permissionDao.getOneById(pid);
        boolean enable = Objects.equals(parent.getEnable(), true);
        ExceptionHandler.assertTrue(enable, CodeEnum.NO, "父节点已被禁用,禁止挂载");

        // 父节点若为叶子节点则更新
        Boolean leafFlag = parent.getLeafFlag();
        if (Objects.equals(leafFlag, true)) {
            Permission permission = new Permission();
            permission.setPermissionId(pid);
            permission.setLeafFlag(false);
            int row = permissionDao.updateById(permission);
            ExceptionHandler.operateDbSingleOk(row, "父节点更新失败");
        }

        // 更新权限信息
        Permission permission = BeanUtil.convert(dto, Permission::new);
        int row = permissionDao.updateById(permission);
        ExceptionHandler.operateDbSingleOk(row, "更新权限信息失败");
        return true;
    }

    /**
     * 删除
     *
     * @param dto dto
     * @return status
     */
    @CacheDelete({RedisKeyEnum.USER_AUTH_RELATION_PERMISSIONS, RedisKeyEnum.USER_AUTH_RELATION_PERMISSION_RESOURCES})
    public Boolean deleteById(DeletePermissionPfDTO dto) {
        Long permissionId = dto.getPermissionId();
        ExceptionHandler.requireNotNull(permissionId, CodeEnum.PARAM_ERROR, "权限ID为空");

        // 检查待删除权限
        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermissionId(permissionId);
        boolean exist = rolePermissionDao.existRelation(rolePermission);
        ExceptionHandler.assertTrue(!exist, CodeEnum.NO, "该权限与角色存在关联, 禁止删除");

        // 记录一下: 这里比较奇怪  好像mybatis自带逻辑删除的话不算受影响行的
        int row = permissionDao.deleteByPermissionId(permissionId);
        ExceptionHandler.operateDbSingleOk(row, "删除权限失败", true);
        return true;
    }

    /**
     * 新增
     *
     * @param dto dto
     * @return status
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheDelete(RedisKeyEnum.USER_AUTH_RELATION_PERMISSIONS)
    public Boolean save(SavePermissionPfDTO dto) {
        // 检查父节点/更新父节点叶子节点标志
        Long pid = dto.getPid();
        Permission parent = permissionDao.getOneById(pid);
        boolean allow = Objects.equals(pid, 0L) || CheckUtil.nonNull(parent);
        ExceptionHandler.requireNotNull(allow, CodeEnum.SERVICE_DATA_ERROR, "父节点权限不存在");
        if (!Objects.equals(pid, 0L) && parent.getLeafFlag()) {
            parent.setLeafFlag(false);
            int row = permissionDao.updateById(parent);
            ExceptionHandler.operateDbSingleOk(row, "更新父节点权限失败");
        }

        Permission permission = BeanUtil.convert(dto, Permission::new);
        permission.setPermissionId(IDUtil.serviceId());
        permission.setLeafFlag(true);
        int row = permissionDao.save(permission);
        ExceptionHandler.operateDbSingleOk(row, "新增权限失败");
        return true;
    }

    /**
     * 切换权限启用状态
     *
     * @param dto dto
     * @return status
     */
    public Boolean toggleEnable(ToggleEnablePermissionPfDTO dto) {
        // 禁用时检查关联状态
        Boolean enable = dto.getEnable();
        if (Objects.equals(enable, false)) {
            Long permissionId = dto.getPermissionId();
            // 权限与角色存在关联时禁止禁用
            RolePermission rp = new RolePermission();
            rp.setPermissionId(permissionId);
            boolean exist = rolePermissionDao.existRelation(rp);
            ExceptionHandler.assertTrue(!exist, CodeEnum.SERVICE_DATA_ERROR, "该权限与角色存在关联, 禁止禁用");
        }

        Permission permission = BeanUtil.convert(dto, Permission::new);
        int row = permissionDao.updateById(permission);
        ExceptionHandler.operateDbSingleOk(row, "切换失败");
        return true;
    }

    /**
     * 根据权限ID查询权限关联的资源
     *
     * @param permissionId permissionId
     * @return list
     */
    public List<RelationResourcesPfVO> listResourcesByPermissionId(Long permissionId) {
        ExceptionHandler.requireNotNull(permissionId, CodeEnum.PARAM_ERROR, "权限ID不能为空");
        List<PermResources> permResources = permResourcesDao.listByPermissionId(permissionId);
        ExceptionHandler.requireNotEmpty(permResources, CodeEnum.SERVICE_DATA_NULL, "该权限不存在");
        List<Long> resourceIds = permResources.stream().map(PermResources::getResourceId).collect(Collectors.toList());
        List<Resources> resources = resourcesDao.listByResourceIds(resourceIds);
        return BeanUtil.listConvert(resources, RelationResourcesPfVO::new);
    }

    /**
     * 绑定资源
     *
     * @param dto dto
     * @return status
     */
    @CacheDelete(RedisKeyEnum.USER_AUTH_RELATION_PERMISSION_RESOURCES)
    public Boolean bindResource(BindResourcePermissionPfDTO dto) {
        Long permissionId = dto.getPermissionId();
        List<PermResources> permResources = permResourcesDao.listByPermissionId(permissionId);
        List<Long> sourceIds = permResources.stream().map(PermResources::getResourceId).collect(Collectors.toList());
        List<Long> resourceIds = dto.getResourceIds();

        // 待添加资源
        List<Long> increaseList = RelationUtil.increaseList(sourceIds, resourceIds);
        if (CheckUtil.nonEmpty(increaseList)) {
            List<PermResources> entityList = increaseList.stream().map(resourceId -> {
                PermResources entity = new PermResources();
                entity.setPermissionId(permissionId);
                entity.setResourceId(resourceId);
                return entity;
            }).collect(Collectors.toList());
            int row = permResourcesDao.saveBatch(entityList);
            ExceptionHandler.operateDbSingleOk(row, "添加资源失败");
        }

        // 待删除资源
        List<Long> reduceList = RelationUtil.reduceList(sourceIds, resourceIds);
        if (CheckUtil.nonEmpty(reduceList)) {
            int row = permResourcesDao.deleteBatch(permissionId, reduceList);
            ExceptionHandler.operateDbMultipleOk(row, "删除资源失败");
        }

        // 删除缓存
        redisUtil.delete(RedisKeyEnum.USER_AUTH_RELATION_ROLE_PERMISSIONS.getKey());
        return true;
    }
}
