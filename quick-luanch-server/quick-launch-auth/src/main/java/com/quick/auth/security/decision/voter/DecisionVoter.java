package com.quick.auth.security.decision.voter;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * @author CShisan
 */
public interface DecisionVoter extends AccessDecisionVoter<RequestAuthorizationContext> {
}
