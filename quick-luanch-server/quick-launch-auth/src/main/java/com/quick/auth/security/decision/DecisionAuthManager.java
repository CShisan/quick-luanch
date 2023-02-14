package com.quick.auth.security.decision;

import com.quick.auth.factory.DecisionVoterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.*;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author CShisan
 */
@Slf4j
@Component
public class DecisionAuthManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final AccessDecisionManager accessDecisionManager;
    private final SecurityMetadataSource securityMetadataSource;

    @Autowired
    public DecisionAuthManager(SecurityMetadataSource securityMetadataSource, DecisionVoterFactory decisionVoterFactory) {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>(decisionVoterFactory.all());
        this.accessDecisionManager = new AffirmativeBased(voters);
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        try {
            Collection<ConfigAttribute> attributes = securityMetadataSource.getAttributes(object);
            this.accessDecisionManager.decide(authentication.get(), object, attributes);
            return new AuthorizationDecision(true);
        } catch (AccessDeniedException ex) {
            return new AuthorizationDecision(false);
        }
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Collection<ConfigAttribute> attributes = securityMetadataSource.getAttributes(object);
        accessDecisionManager.decide(authentication.get(), object, attributes);
    }
}
