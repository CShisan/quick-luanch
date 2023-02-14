package com.quick.auth.security.decision.voter;

import com.quick.auth.security.datasource.SecurityConfigAttribute;
import com.quick.common.utils.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yuanbai
 */
@Component
public class PermissionVoter implements DecisionVoter {
    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    private final PermissionUtil permissionUtil;

    @Autowired
    public PermissionVoter(PermissionUtil permissionUtil) {
        this.permissionUtil = permissionUtil;
    }

    @Override
    public int vote(Authentication authentication, RequestAuthorizationContext object, Collection<ConfigAttribute> attributes) {
        // 若是匿名用户直接投票不同意
        if (authentication instanceof AnonymousAuthenticationToken) {
            return ACCESS_DENIED;
        }
        String url = Optional.ofNullable(object)
                .map(RequestAuthorizationContext::getRequest)
                .map(HttpServletRequest::getRequestURI).orElse("");
        for (ConfigAttribute configAttribute : attributes) {
            SecurityConfigAttribute attribute = (SecurityConfigAttribute) configAttribute;
            String path = attribute.getPath();
            // 匹配到需要校验的资源路径则进行权限校验
            if (MATCHER.match(url, path)) {
                List<String> required = attribute.getPermissions();
                Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
                List<String> owns = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
                // 判断是否有交集,有则投票同意
                return permissionUtil.access(owns, required) ? ACCESS_GRANTED : ACCESS_DENIED;
            }
        }
        // 若不是需要验证的资源路径则放行
        return ACCESS_GRANTED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof SecurityConfigAttribute;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RequestAuthorizationContext.class.isAssignableFrom(clazz);
    }
}
