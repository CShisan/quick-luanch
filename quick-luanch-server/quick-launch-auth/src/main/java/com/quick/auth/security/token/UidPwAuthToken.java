package com.quick.auth.security.token;

import com.quick.auth.entity.UidPwEntity;

/**
 * @author yuanbai
 */
public class UidPwAuthToken extends AuthToken {
    public UidPwAuthToken(String json) {
        super(json, UidPwEntity.class);
    }

    @Override
    public UidPwEntity getPrincipal() {
        return (UidPwEntity) this.principal;
    }
}
