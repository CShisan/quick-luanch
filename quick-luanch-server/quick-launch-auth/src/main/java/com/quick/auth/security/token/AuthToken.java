package com.quick.auth.security.token;

import com.quick.auth.entity.LoginEntity;
import com.quick.common.utils.JsonUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author CShisan
 */
public class AuthToken extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID = 561L;
    protected final LoginEntity principal;

    public AuthToken(String json, Class<? extends LoginEntity> clazz) {
        this(JsonUtil.toEntity(json, clazz));
    }

    public AuthToken(LoginEntity principal) {
        super(null);
        this.principal = principal;
        this.setAuthenticated(false);
    }

    public AuthToken(LoginEntity principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public LoginEntity getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
