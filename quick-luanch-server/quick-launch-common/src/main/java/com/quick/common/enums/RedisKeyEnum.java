package com.quick.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanbai
 */
@Getter
@AllArgsConstructor
public enum RedisKeyEnum {
    /**
     * Redis的key枚举(等级划分: 系统 > 用户)
     * 等级定义:
     * 系统: 适用于系统的参数
     * 用户: 适用于用户的相关参数
     */
    USER_AUTH_TOKEN("user:auth:token:%s:%s", "用户登录token"),
    USER_AUTH_RELATION_ROLES("user:auth:relation:roles", "用户角色继承的映射关系"),
    USER_AUTH_RELATION_ROLE_PERMISSIONS("user:auth:relation:role-permissions", "用户角色与权限的映射关系"),
    USER_AUTH_RELATION_PERMISSIONS("user:auth:relation:permissions", "用户权限继承的映射关系"),
    USER_AUTH_RELATION_PERMISSION_RESOURCES("user:auth:relation:permission-resources", "用户权限与资源的映射关系"),
    USER_AUTH_RELATION_RESOURCES("user:auth:relation:resources", "用户资源继承的映射关系"),
    USER_AUTH_RELATION_RESOURCE_PERMISSIONS("user:auth:relation:resource-permissions", "用户资源与权限的映射关系"),


    SYSTEM_FILE_UPLOAD_MULTIPART_NAME("system:file:upload:multipart:name:%s", "系统文件分片上传名称");


    private final String key;
    private final String desc;

    public String format(Object... params) {
        return String.format(this.key, params);
    }
}
