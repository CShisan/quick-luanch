package com.quick.auth.security.provider;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.entity.UidPwEntity;
import com.quick.auth.security.datasource.UserDetailsService;
import com.quick.auth.security.token.UidPwAuthToken;
import com.quick.auth.utils.AuthCaptchaUtil;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.domain.dao.UserDao;
import com.quick.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author yuanbai
 */
@Component
public class UidPwAuthProvider extends AbstractPwAuthProvider {
    private final AuthCaptchaUtil authCaptchaUtil;

    @Autowired
    public UidPwAuthProvider(UserDetailsService userDetailsService, UserDao userDao, AuthCaptchaUtil authCaptchaUtil) {
        super(userDetailsService, userDao);
        this.authCaptchaUtil = authCaptchaUtil;
    }

    @Override
    protected LoginEntity login(Authentication authentication) {
        Assert.isInstanceOf(UidPwAuthToken.class, authentication, "不支持该认证类型");
        UidPwEntity entity = ((UidPwAuthToken) authentication).getPrincipal();
        ExceptionHandler.requireNotNull(entity, CodeEnum.USER_AUTH_ERROR);

        // 验证码验证
        boolean validCaptcha = authCaptchaUtil.validateCaptcha(entity.getCaptcha());
        ExceptionHandler.assertTrue(validCaptcha, CodeEnum.USER_AUTH_ERROR, "验证码错误");

        // 查询user进行密码验证
        Long uid = entity.getUid();
        ExceptionHandler.requireNotNull(uid, CodeEnum.USER_AUTH_ERROR, "用户ID不能为空");
        User user = new User();
        user.setUid(uid);
        return pwLogin(user, entity.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UidPwAuthToken.class.isAssignableFrom(authentication);
    }
}
