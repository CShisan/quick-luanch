package com.quick.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanbai
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    /**
     * 角色类型枚举
     */
    ADMIN(1L, "admin", "超级管理员"),
    MANAGER(2L, "manager", "管理员"),
    MAINTAINER(3L, "maintainer", "系统维护员"),
    NORMAL(4L, "normal", "普通用户");


    private final long roleId;
    private final String roleKey;
    private final String roleName;
}
