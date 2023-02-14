package com.quick.auth.factory;

import com.quick.auth.enums.LoginStrategyEnum;
import com.quick.auth.security.provider.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author yuanbai
 */
@Component
public class AuthProviderFactory {
    private static final Map<LoginStrategyEnum, AuthenticationProvider> RELATION = new HashMap<>(16);

    @Autowired
    public AuthProviderFactory(List<AuthProvider> providers) {
        for (AuthProvider provider : providers) {
            LoginStrategyEnum strategy = LoginStrategyEnum.typeOf(provider.getClass());
            Optional.ofNullable(strategy).ifPresent(item -> RELATION.put(item, provider));
        }
    }

    public Collection<AuthenticationProvider> all() {
        return RELATION.values();
    }
}
