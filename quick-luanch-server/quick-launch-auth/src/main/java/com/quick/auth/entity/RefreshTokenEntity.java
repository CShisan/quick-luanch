package com.quick.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yuanbai
 */
@Data
@Schema(description = "刷新token实体")
public class RefreshTokenEntity {
    @Schema(description = "登录实体")
    private LoginEntity entity;

    @Schema(description = "token")
    private String token;

    @Schema(description = "刷新token")
    private String refreshToken;
}
