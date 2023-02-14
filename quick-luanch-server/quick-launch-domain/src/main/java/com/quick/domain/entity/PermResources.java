package com.quick.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "perm_resources", autoResultMap = true)
public class PermResources extends BaseEntity {
    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 资源ID
     */
    private Long resourceId;
}
