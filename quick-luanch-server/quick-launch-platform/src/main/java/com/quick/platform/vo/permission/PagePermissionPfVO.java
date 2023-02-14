package com.quick.platform.vo.permission;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author CShisan
 */
@Data
@Schema(description = "权限信息分页VO")
public class PagePermissionPfVO {
    @Schema(description = "权限ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long permissionId;

    @Schema(description = "权限KEY")
    private String permissionKey;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "父ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;

    @Schema(description = "叶子结点标记")
    private Boolean leafFlag;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
