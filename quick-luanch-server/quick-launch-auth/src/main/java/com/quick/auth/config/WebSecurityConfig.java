package com.quick.auth.config;

import com.quick.auth.security.decision.DecisionAuthManager;
import com.quick.auth.security.filter.JwtTokenAuthFilter;
import com.quick.auth.security.filter.LoginAuthFilter;
import com.quick.auth.security.handler.AuthDeniedHandler;
import com.quick.auth.security.handler.LogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.util.Arrays;

/**
 * @author CShisan
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtTokenAuthFilter jwtTokenAuthFilter;
    private final LogoutHandler logoutHandler;
    private final LoginAuthFilter loginAuthFilter;
    private final DecisionAuthManager authorizationManager;

    @Autowired
    public WebSecurityConfig(JwtTokenAuthFilter jwtTokenAuthFilter, LogoutHandler logoutHandler, LoginAuthFilter loginAuthFilter, DecisionAuthManager authorizationManager) {
        this.jwtTokenAuthFilter = jwtTokenAuthFilter;
        this.logoutHandler = logoutHandler;
        this.loginAuthFilter = loginAuthFilter;
        this.authorizationManager = authorizationManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 使用jwt关闭csrf、配置白名单、配置决策管理器
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .mvcMatchers(allowList())
                .permitAll()
                .anyRequest()
                .access(authorizationManager);

        // token预认证处理器、自定义认证处理器
        http
                .addFilterAt(jwtTokenAuthFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAt(loginAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // 授权失败处理器
        http.exceptionHandling().accessDeniedHandler(new AuthDeniedHandler());

        // 登出处理器
        http.logout().logoutSuccessHandler(logoutHandler);

        return http.build();
    }

    /**
     * 白名单路径放行(不走SpringSecurity过滤链)
     */
    @Bean
    public WebSecurityCustomizer securityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/webjars/*", "/swagger-resources/*", "/v3/api-docs/*", "/swagger-ui/*", "/swagger-ui.html");
    }

    /**
     * 白名单路径放行(走SpringSecurity过滤链)
     */
    private String[] allowList() {
        return Arrays.asList("/test/*", "/register", "/auth/refresh-token", "/auth/captcha").toArray(new String[0]);
    }
}
