package com.quick.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.*;
import com.quick.domain.dao.RoleDao;
import com.quick.domain.dao.UserDao;
import com.quick.domain.dao.UserRoleDao;
import com.quick.domain.entity.Role;
import com.quick.domain.entity.User;
import com.quick.domain.entity.UserRole;
import com.quick.domain.enums.ResourceTypeEnum;
import com.quick.domain.enums.RoleEnum;
import com.quick.platform.dto.user.PageUserPfDTO;
import com.quick.platform.dto.user.ToggleEnableUserPfDTO;
import com.quick.platform.dto.user.ToggleLockUserPfDTO;
import com.quick.platform.dto.user.UpdateUserPfDTO;
import com.quick.platform.vo.role.InfoRolePfVO;
import com.quick.platform.vo.role.ListRelationRolePfVO;
import com.quick.platform.vo.user.DetailUserPfVO;
import com.quick.platform.vo.user.PageUserPfVO;
import com.quick.platform.vo.user.PersonalInfoPfVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Service
public class UserService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final RoleUtil roleUtil;
    private final PermissionUtil permissionUtil;
    private final ResourceUtil resourceUtil;

    @Autowired
    public UserService(UserDao userDao, RoleDao roleDao, UserRoleDao userRoleDao, RoleUtil roleUtil, PermissionUtil permissionUtil, ResourceUtil resourceUtil) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.roleUtil = roleUtil;
        this.permissionUtil = permissionUtil;
        this.resourceUtil = resourceUtil;
    }

    /**
     * 获取个人信息
     *
     * @param uid uid
     * @return info
     */
    public PersonalInfoPfVO personalInfo(Long uid) {
        // 查询user
        ExceptionHandler.requireNotNull(uid, CodeEnum.SERVICE_DATA_NULL);
        PersonalInfoPfVO vo = BeanUtil.convert(userDao.getOneById(uid), PersonalInfoPfVO::new);
        ExceptionHandler.requireNotNull(vo, CodeEnum.SERVICE_DATA_ERROR, "用户不存在");

        // 查询拥有角色
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        ExceptionHandler.requireNotEmpty(roleIds, CodeEnum.SERVICE_DATA_ERROR, "用户角色异常");
        List<RelationUtil.Relation> roles = roleUtil.relationBy(roleIds);
        List<String> roleKeys = roles.stream().map(RelationUtil.Relation::getKey).collect(Collectors.toList());
        vo.setRoles(roleKeys);

        List<RelationUtil.Relation> rolePermissions = roleUtil.bindRelationBy(roleIds);
        if (CheckUtil.nonEmpty(rolePermissions)) {
            // 查询权限信息
            List<Long> permissionIds = rolePermissions.stream()
                    .map(RelationUtil.Relation::getRelationIds)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            // 获取全部权限id
            List<Long> fullPermIds = permissionUtil.fullIds(permissionIds);
            List<RelationUtil.Relation> permissions = permissionUtil.relationBy(fullPermIds);
            Set<String> permissionKeys = permissions.stream().map(RelationUtil.Relation::getKey).collect(Collectors.toSet());
            ExceptionHandler.requireNotEmpty(permissionKeys, CodeEnum.SERVICE_DATA_ERROR, "用户权限异常");
            vo.setPermissions(permissionKeys);

            // 查询权限绑定的资源
            List<RelationUtil.Relation> permResources = permissionUtil.bindRelationBy(fullPermIds);
            List<Long> resourceIds = permResources.stream()
                    .map(RelationUtil.Relation::getRelationIds)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            List<RelationUtil.Relation> resources = resourceUtil.listBy(resourceIds, ResourceTypeEnum.FRONT_END);
            Set<String> resourceKeys = resources.stream().map(RelationUtil.Relation::getKey).collect(Collectors.toSet());
            vo.setResources(resourceKeys);
        }
        return vo;
    }

    /**
     * 用户信息分页
     *
     * @param dto dto
     * @return page
     */
    public IPage<PageUserPfVO> page(PageUserPfDTO dto) {
        User entity = BeanUtil.convert(dto, User::new);
        IPage<User> page = userDao.page(entity, dto.page());
        return PageUtil.convert(page, PageUserPfVO::new);
    }

    /**
     * 更新用户信息
     *
     * @param dto dto
     * @return status
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateById(UpdateUserPfDTO dto) {
        // 检查并更新角色
        this.checkValidRole(dto);
        Long uid = dto.getUid();
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        List<Long> currentRole = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Long> roleIds = dto.getRoleIds();

        // 待添加的角色
        List<Long> addRoles = new ArrayList<>(roleIds);
        addRoles.removeAll(currentRole);
        if (CheckUtil.nonEmpty(addRoles)) {
            addRoles.forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUid(uid);
                userRole.setRoleId(roleId);
                int row = userRoleDao.save(userRole);
                ExceptionHandler.operateDbSingleOk(row, "添加角色失败");
            });
        }
        // 待移除的角色
        List<Long> subRoles = new ArrayList<>(currentRole);
        subRoles.removeAll(roleIds);
        if (CheckUtil.nonEmpty(subRoles)) {
            int row = userRoleDao.deleteBatch(uid, subRoles);
            ExceptionHandler.operateDbMultipleOk(row, "删除角色失败");
        }

        // 用户信息校验
        String phone = dto.getPhone();
        if (CheckUtil.nonBlank(phone)) {
            User check = userDao.getOneByPhone(phone);
            ExceptionHandler.assertTrue(CheckUtil.isNull(check), CodeEnum.NO, "该手机号已绑定其他账号");
        }
        String email = dto.getEmail();
        if (CheckUtil.nonBlank(email)) {
            User check = userDao.getOneByEmail(email);
            ExceptionHandler.assertTrue(CheckUtil.isNull(check), CodeEnum.NO, "该邮箱已绑定其他账号");
        }

        User user = BeanUtil.convert(dto, User::new);
        return userDao.updateById(user);
    }

    /**
     * 检查角色有效性
     *
     * @param dto dto
     */
    private void checkValidRole(UpdateUserPfDTO dto) {
        List<Long> roleIds = dto.getRoleIds();
        if (CheckUtil.nonEmpty(roleIds)) {
            List<UserRole> userRoles = userRoleDao.listByUid(dto.getUid());
            List<Long> currentRole = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

            // 更新角色为管理员,需要超级管理员角色
            if (roleIds.contains(RoleEnum.MANAGER.getRoleId())) {
                boolean allow = currentRole.contains(RoleEnum.ADMIN.getRoleId());
                ExceptionHandler.assertTrue(allow, CodeEnum.NO, "非法分配角色");
            }

            // 更新角色为系统维护员,需要白名单角色
            if (roleIds.contains(RoleEnum.MAINTAINER.getRoleId())) {
                List<Long> whitelist = Arrays.asList(RoleEnum.ADMIN.getRoleId(), RoleEnum.MANAGER.getRoleId());
                boolean allow = whitelist.containsAll(currentRole);
                ExceptionHandler.assertTrue(allow, CodeEnum.NO, "非法分配角色");
            }
        }
    }

    /**
     * 根据uid查询单个用户
     *
     * @param uid uid
     * @return vo
     */
    public DetailUserPfVO getOneById(Long uid) {
        ExceptionHandler.requireNotNull(uid, CodeEnum.PARAM_ERROR, "UID不能为空");
        User user = userDao.getOneById(uid);
        ExceptionHandler.requireNotNull(user, CodeEnum.NO, "获取用户详情失败");

        // 查询拥有角色
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        List<InfoRolePfVO> roles = new ArrayList<>();
        if (CheckUtil.nonEmpty(userRoles)) {
            roles = userRoles.stream().map(userRole -> {
                Role role = roleDao.getOneById(userRole.getRoleId());
                return BeanUtil.convert(role, InfoRolePfVO::new);
            }).collect(Collectors.toList());
        }

        DetailUserPfVO vo = BeanUtil.convert(user, DetailUserPfVO::new);
        vo.setRoles(roles);
        return vo;
    }

    /**
     * 切换用户启用状态
     *
     * @param dto dto
     * @return status
     */
    public Boolean toggleEnable(ToggleEnableUserPfDTO dto) {
        User user = BeanUtil.convert(dto, User::new);
        int row = userDao.updateById(user);
        ExceptionHandler.operateDbSingleOk(row, "切换失败");
        return true;
    }

    /**
     * 切换用户启用状态
     *
     * @param dto dto
     * @return status
     */
    public Boolean toggleLock(ToggleLockUserPfDTO dto) {
        User user = BeanUtil.convert(dto, User::new);
        int row = userDao.updateById(user);
        ExceptionHandler.operateDbSingleOk(row, "切换失败");
        return true;
    }

    /**
     * 根据uid查询用户关联的角色列表
     *
     * @param uid uid
     * @return list
     */
    public List<ListRelationRolePfVO> getRolesByUid(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        return BeanUtil.listConvert(userRoles, ListRelationRolePfVO::new);
    }
}
