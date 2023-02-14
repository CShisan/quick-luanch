package com.quick.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role", autoResultMap = true)
public class Role extends BaseEntity {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色KEY
     */
    private String roleKey;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否启用
     */
    private Boolean enable;
}
