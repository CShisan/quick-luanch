package com.quick.auth.security.filter;

import com.quick.auth.enums.LoginStrategyEnum;
import com.quick.auth.factory.AuthProviderFactory;
import com.quick.auth.security.handler.AuthFailHandler;
import com.quick.auth.security.handler.AuthSuccessHandler;
import com.quick.auth.security.token.AuthToken;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author CShisan
 */
@Slf4j
@Component
public class LoginAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher PATH_REQUEST = new AntPathRequestMatcher("/login", "POST");
    private static final String REQUEST_METHOD = "POST";
    private static final String METHOD_NOT_SUPPORT_TIPS_PREFIX = "不支持该请求方法: ";
    private static final String CONTENT_TYPE_NOT_SUPPORT_TIPS = "只支持JSON类型请求";
    private final AuthSuccessHandler authSuccessHandler;
    private final AuthFailHandler authFailHandler;

    @Autowired
    public LoginAuthFilter(AuthProviderFactory providerFactory, AuthSuccessHandler authSuccessHandler, AuthFailHandler authFailHandler) {
        super(PATH_REQUEST, authManager(providerFactory));
        this.authSuccessHandler = authSuccessHandler;
        this.authFailHandler = authFailHandler;
    }

    @PostConstruct
    protected void init() {
        this.setAuthenticationSuccessHandler(authSuccessHandler);
        this.setAuthenticationFailureHandler(authFailHandler);
    }

    protected static AuthenticationManager authManager(AuthProviderFactory providerFactory) {
        Collection<AuthenticationProvider> providers = providerFactory.all();
        return new ProviderManager(new ArrayList<>(providers));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 只支持POST请求
        boolean isPost = Objects.equals(request.getMethod(), REQUEST_METHOD);
        ExceptionHandler.assertTrue(isPost, CodeEnum.USER_AUTH_ERROR, METHOD_NOT_SUPPORT_TIPS_PREFIX.concat(request.getMethod()));
        // 只支持JSON类型
        boolean isJson = Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
        ExceptionHandler.assertTrue(isJson, CodeEnum.USER_AUTH_ERROR, CONTENT_TYPE_NOT_SUPPORT_TIPS);

        // 设置登录信息
        String body = HttpUtil.getBody(request);
        AuthToken authToken = LoginStrategyEnum.token(body);
        authToken.setDetails(super.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authToken);
    }

}
