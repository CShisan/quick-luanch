package com.quick.auth.security.token;

import com.quick.auth.entity.WxLoginEntity;

/**
 * @author yuanbai
 */
public class WxAppletAuthToken extends AuthToken {
    public WxAppletAuthToken(String json) {
        super(json, WxLoginEntity.class);
    }

    @Override
    public WxLoginEntity getPrincipal() {
        return (WxLoginEntity) this.principal;
    }
}
