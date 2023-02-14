package com.quick.platform.vo.permission;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author CShisan
 */
@Data
@Schema(description = "角色关联的权限列表VO")
public class ListRelationPermissionPfVO {
    @Schema(description = "权限ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long permissionId;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "叶子节点标记")
    private Boolean leafFlag;
}
