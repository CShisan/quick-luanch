package com.quick.auth.enums;

import com.quick.auth.security.decision.voter.DecisionVoter;
import com.quick.auth.security.decision.voter.PermissionVoter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CShisan
 */
@Getter
@AllArgsConstructor
public enum DecisionVoterEnum {
    /**
     * 决策选民枚举
     */
    PERMISSION("permission", "权限验证选民", PermissionVoter.class);

    private final String type;
    private final String desc;
    private final Class<? extends DecisionVoter> clazz;
    private static final Map<Class<? extends DecisionVoter>, DecisionVoterEnum> RELATION = new HashMap<>(16);

    static {
        for (DecisionVoterEnum value : values()) {
            RELATION.put(value.clazz, value);
        }
    }

    /**
     * 根据class获取对应voter
     *
     * @param clazz clazz
     * @return voter
     */
    public static DecisionVoterEnum clazzOf(Class<? extends DecisionVoter> clazz) {
        return RELATION.get(clazz);
    }
}
