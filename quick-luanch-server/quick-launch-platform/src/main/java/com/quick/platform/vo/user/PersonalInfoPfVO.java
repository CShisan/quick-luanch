package com.quick.platform.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author CShisan
 */
@Data
@Schema(description = "个人信息")
public class PersonalInfoPfVO {
    @Schema(description = "UID", type = "string")
    private Long uid;

    @Schema(description = "角色集")
    private List<String> roles;

    @Schema(description = "权限集")
    private Set<String> permissions;

    @Schema(description = "资源集")
    private Set<String> resources;
}
