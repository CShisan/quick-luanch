package com.quick.auth.security.provider;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.security.datasource.UserDetailsService;
import com.quick.auth.security.token.AuthToken;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.IpUtil;
import com.quick.domain.dao.UserDao;
import com.quick.domain.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

/**
 * @author CShisan
 */
public abstract class AbstractAuthProvider implements AuthProvider {
    private final UserDetailsService userDetailsService;
    protected final UserDao userDao;

    public AbstractAuthProvider(UserDetailsService userDetailsService, UserDao userDao) {
        this.userDetailsService = userDetailsService;
        this.userDao = userDao;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        // 登录
        LoginEntity entity = this.login(authentication);
        ExceptionHandler.requireNotNull(entity);
        Long uid = entity.getUid();

        // 获取权限
        entity = (LoginEntity) userDetailsService.loadUserByUid(uid);
        entity.setPassword("");

        // 设置认证信息
        AuthToken authToken = new AuthToken(entity, entity.getAuthorities());
        authToken.setDetails(authentication.getDetails());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 更新最后登录时间、IP
        this.updateLoginInfo(uid);

        return authToken;
    }

    /**
     * 登录
     *
     * @param authentication authentication
     * @return entity
     */
    protected abstract LoginEntity login(Authentication authentication);

    /**
     * 更新最后登录时间、IP
     *
     * @param uid uid
     */
    private void updateLoginInfo(Long uid) {
        User user = new User();
        user.setUid(uid);
        user.setLoginLastIp(IpUtil.getIpAddress());
        user.setLoginLastTime(new Date());
        int row = userDao.updateById(user);
        ExceptionHandler.assertTrue(row == 1, CodeEnum.NO);
    }
}
