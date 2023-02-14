package com.quick.auth.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.quick.common.utils.CheckUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CShisan
 */
@Data
@Schema(description = "登录实体")
public class LoginEntity implements UserDetails {
    @Schema(description = "用户ID", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户名称")
    private String username;

    @Schema(description = "启用标志")
    private Boolean enable;

    @Schema(description = "锁定标志")
    private Boolean locked;

    @Schema(description = "删除标识")
    private Boolean deleted;

    @Schema(description = "角色集")
    private List<String> roles;

    @Schema(description = "权限集")
    private Set<String> permissions;

    @Schema(description = "权限集")
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CheckUtil.anyEmpty(roles, permissions)) {
            return new HashSet<>();
        }
        if (CheckUtil.nonEmpty(authorities)) {
            return authorities;
        }
        return permissions.stream().map(permission -> (GrantedAuthority) () -> permission).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(locked, false);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(enable, true);
    }
}
