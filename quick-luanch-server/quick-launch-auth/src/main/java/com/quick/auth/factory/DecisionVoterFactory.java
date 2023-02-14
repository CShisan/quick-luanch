package com.quick.auth.factory;

import com.quick.auth.enums.DecisionVoterEnum;
import com.quick.auth.security.decision.voter.DecisionVoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author CShisan
 */
@Component
public class DecisionVoterFactory {
    private static final Map<String, AccessDecisionVoter<RequestAuthorizationContext>> RELATION = new HashMap<>(16);

    @Autowired
    public DecisionVoterFactory(List<DecisionVoter> voters) {
        for (DecisionVoter voter : voters) {
            DecisionVoterEnum decision = DecisionVoterEnum.clazzOf(voter.getClass());
            Optional.ofNullable(decision).ifPresent(item -> RELATION.put(item.getType(), voter));
        }
    }

    public Collection<AccessDecisionVoter<RequestAuthorizationContext>> all() {
        return RELATION.values();
    }
}
