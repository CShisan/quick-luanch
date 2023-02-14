package com.quick.auth.security.token;

import com.quick.auth.entity.PhonePwEntity;

/**
 * @author yuanbai
 */
public class PhonePwAuthToken extends AuthToken {
    public PhonePwAuthToken(String json) {
        super(json, PhonePwEntity.class);
    }

    @Override
    public PhonePwEntity getPrincipal() {
        return (PhonePwEntity) this.principal;
    }
}
