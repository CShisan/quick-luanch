package com.quick.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.aop.annotation.CacheDelete;
import com.quick.common.enums.CodeEnum;
import com.quick.common.enums.RedisKeyEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.*;
import com.quick.domain.dao.RoleDao;
import com.quick.domain.dao.RolePermissionDao;
import com.quick.domain.dao.UserRoleDao;
import com.quick.domain.entity.Role;
import com.quick.domain.entity.RolePermission;
import com.quick.domain.entity.UserRole;
import com.quick.platform.dto.role.*;
import com.quick.platform.vo.permission.ListRelationPermissionPfVO;
import com.quick.platform.vo.role.DetailRolePfVO;
import com.quick.platform.vo.role.PageRolePfVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Service
public class RoleService {
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final RolePermissionDao rolePermissionDao;
    private final RoleUtil roleUtil;

    @Autowired
    public RoleService(RoleDao roleDao, UserRoleDao userRoleDao, RolePermissionDao rolePermissionDao, RoleUtil roleUtil) {
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.rolePermissionDao = rolePermissionDao;
        this.roleUtil = roleUtil;
    }

    /**
     * 后台角色信息分页
     *
     * @param dto dto
     * @return page
     */
    public IPage<PageRolePfVO> page(PageRolePfDTO dto) {
        Role entity = BeanUtil.convert(dto, Role::new);
        IPage<Role> page = roleDao.page(entity, dto.page());
        return PageUtil.convert(page, PageRolePfVO::new);
    }

    /**
     * 删除
     *
     * @param dto dto
     * @return status
     */
    @CacheDelete({RedisKeyEnum.USER_AUTH_RELATION_ROLES, RedisKeyEnum.USER_AUTH_RELATION_ROLE_PERMISSIONS})
    public Boolean deleteById(DeleteRolePfDTO dto) {
        Long roleId = dto.getRoleId();
        ExceptionHandler.requireNotNull(roleId, CodeEnum.PARAM_ERROR, "角色ID不能为空");

        // 检查待删除角色
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        boolean exist = userRoleDao.existRelation(userRole);
        ExceptionHandler.assertTrue(!exist, CodeEnum.NO, "该角色与用户存在关联, 禁止删除");
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        exist = rolePermissionDao.existRelation(rolePermission);
        ExceptionHandler.assertTrue(!exist, CodeEnum.NO, "该角色与权限存在关联, 禁止删除");


        int row = roleDao.deleteById(roleId);
        ExceptionHandler.operateDbSingleOk(row, "删除角色失败", true);
        return true;
    }

    /**
     * 根据roleId查询单个角色
     *
     * @param roleId roleId
     * @return vo
     */
    public DetailRolePfVO getOneById(Long roleId) {
        ExceptionHandler.requireNotNull(roleId, CodeEnum.SERVICE_DATA_NULL, "角色ID不能为空");
        Role role = roleDao.getOneById(roleId);
        ExceptionHandler.requireNotNull(role, CodeEnum.SERVICE_DATA_NULL, "获取角色详情失败");
        return BeanUtil.convert(role, DetailRolePfVO::new);
    }

    /**
     * 根据roleId查询角色关联的权限列表
     *
     * @param roleId roleId
     * @return list
     */
    public List<ListRelationPermissionPfVO> getPermissionsByRoleId(Long roleId) {
        ExceptionHandler.requireNotNull(roleId, CodeEnum.PARAM_ERROR, "角色ID不能为空");
        List<RelationUtil.Relation> binds = roleUtil.bindRelationBy(Collections.singletonList(roleId));
        return binds.stream().flatMap(item -> item.getRelations().stream()).map(bind -> {
            ListRelationPermissionPfVO vo = new ListRelationPermissionPfVO();
            vo.setPermissionId(bind.getId());
            vo.setPermissionName(bind.getKey());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 更新角色关联的权限
     *
     * @param dto dto
     * @return status
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheDelete(RedisKeyEnum.USER_AUTH_RELATION_ROLE_PERMISSIONS)
    public Boolean updateRolePermission(UpdateRolePermissionPfDTO dto) {
        Long roleId = dto.getRoleId();
        List<RolePermission> rolePermissions = rolePermissionDao.listByRoleId(roleId);
        List<Long> sourceIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Long> permissionIds = dto.getPermissionIds();

        // 待添加权限
        List<Long> increaseList = RelationUtil.increaseList(sourceIds, permissionIds);
        if (CheckUtil.nonEmpty(increaseList)) {
            List<RolePermission> entityList = increaseList.stream().map(permissionId -> {
                RolePermission entity = new RolePermission();
                entity.setRoleId(roleId);
                entity.setPermissionId(permissionId);
                return entity;
            }).collect(Collectors.toList());
            int row = rolePermissionDao.saveBatch(entityList);
            ExceptionHandler.operateDbSingleOk(row, "添加权限失败");
        }

        // 待删除权限
        List<Long> reduceList = RelationUtil.reduceList(sourceIds, permissionIds);
        if (CheckUtil.nonEmpty(reduceList)) {
            int row = rolePermissionDao.deleteBatch(roleId, reduceList);
            ExceptionHandler.operateDbMultipleOk(row, "删除权限失败");
        }
        return true;
    }

    /**
     * 根据roleId更新
     *
     * @param dto dto
     * @return status
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheDelete({RedisKeyEnum.USER_AUTH_RELATION_ROLES, RedisKeyEnum.USER_AUTH_RELATION_ROLE_PERMISSIONS})
    public Boolean updateById(UpdateRolePfDTO dto) {
        int row = roleDao.updateById(BeanUtil.convert(dto, Role::new));
        ExceptionHandler.operateDbSingleOk(row, "角色信息更新失败");
        return true;
    }

    /**
     * 检查角色启用状态
     *
     * @param roleId roleId
     */
    private void checkRoleEnable(Long roleId) {
        ExceptionHandler.requireNotNull(roleId, CodeEnum.SERVICE_DATA_ERROR, "角色不存在");
        Role role = roleDao.getOneById(roleId);
        ExceptionHandler.requireNotNull(role, CodeEnum.SERVICE_DATA_ERROR, "角色不存在");
        Boolean enable = role.getEnable();
        ExceptionHandler.assertTrue(enable, CodeEnum.SERVICE_DATA_ERROR, "角色已被禁用");
    }

    /**
     * 新增角色
     *
     * @param dto dto
     * @return status
     */
    @CacheDelete(RedisKeyEnum.USER_AUTH_RELATION_ROLES)
    public Boolean save(SaveRolePfDTO dto) {
        Role role = BeanUtil.convert(dto, Role::new);
        role.setRoleId(IDUtil.serviceId());
        int row = roleDao.save(role);
        ExceptionHandler.operateDbSingleOk(row, "新增角色失败");
        return true;
    }

    /**
     * 切换角色启用状态
     *
     * @param dto dto
     * @return status
     */
    public Boolean toggleEnable(ToggleEnableRolePfDTO dto) {
        // 禁用时检查关联状态
        Boolean enable = dto.getEnable();
        if (Objects.equals(enable, false)) {
            Long roleId = dto.getRoleId();
            // 角色与用户存在关联时禁止禁用
            UserRole ur = new UserRole();
            ur.setRoleId(roleId);
            boolean exist = userRoleDao.existRelation(ur);
            ExceptionHandler.assertTrue(!exist, CodeEnum.SERVICE_DATA_ERROR, "该角色与用户存在关联, 禁止禁用");
            // 角色与权限存在关联时禁止禁用
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            exist = rolePermissionDao.existRelation(rp);
            ExceptionHandler.assertTrue(!exist, CodeEnum.SERVICE_DATA_ERROR, "该角色与权限存在关联, 禁止禁用");
        }

        Role role = BeanUtil.convert(dto, Role::new);
        int row = roleDao.updateById(role);
        ExceptionHandler.operateDbSingleOk(row, "切换失败");
        return true;
    }
}
