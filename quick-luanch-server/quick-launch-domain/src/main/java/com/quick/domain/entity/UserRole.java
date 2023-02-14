package com.quick.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yuanbai
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user_role", autoResultMap = true)
public class UserRole extends BaseEntity {
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 角色ID
     */
    private Long roleId;
}
