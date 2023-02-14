package com.quick.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.quick.domain.enums.ResourceTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CShisan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "resources", autoResultMap = true)
public class Resources extends BaseEntity {
    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 资源类型
     */
    private ResourceTypeEnum type;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 启用标志
     */
    private Boolean enable;
}
