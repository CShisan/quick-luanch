package com.quick.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "permission", autoResultMap = true)
public class Permission extends BaseEntity {
    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限KEY
     */
    private String permissionKey;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 叶子节点标记
     */
    private Boolean leafFlag;

    /**
     * 是否启用
     */
    private Boolean enable;
}
