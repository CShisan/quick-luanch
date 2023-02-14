package com.quick.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


/**
 * @author CShisan
 */
@Data
public class BaseEntity {
    /**
     * 自增id,主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 删除标识,0未删除,1已删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
    /**
     * 编辑者id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long editor;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
