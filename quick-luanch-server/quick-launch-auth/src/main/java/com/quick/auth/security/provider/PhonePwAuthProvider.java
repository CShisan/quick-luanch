package com.quick.auth.security.provider;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.entity.PhonePwEntity;
import com.quick.auth.security.datasource.UserDetailsService;
import com.quick.auth.security.token.PhonePwAuthToken;
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
public class PhonePwAuthProvider extends AbstractPwAuthProvider {
    private final AuthCaptchaUtil authCaptchaUtil;

    @Autowired
    public PhonePwAuthProvider(UserDetailsService userDetailsService, UserDao userDao, AuthCaptchaUtil authCaptchaUtil) {
        super(userDetailsService, userDao);
        this.authCaptchaUtil = authCaptchaUtil;
    }

    @Override
    protected LoginEntity login(Authentication authentication) {
        Assert.isInstanceOf(PhonePwAuthToken.class, authentication, "不支持该认证类型");
        PhonePwEntity entity = ((PhonePwAuthToken) authentication).getPrincipal();
        ExceptionHandler.requireNotNull(entity);
        // 验证码验证
        boolean validCaptcha = authCaptchaUtil.validateCaptcha(entity.getCaptcha());
        ExceptionHandler.assertTrue(validCaptcha, CodeEnum.USER_AUTH_ERROR, "验证码错误");

        // 查询user进行密码验证
        String phone = entity.getPhone();
        ExceptionHandler.requireNotBlank(phone, CodeEnum.USER_AUTH_ERROR, "手机号码不能为空");
        User user = new User();
        user.setPhone(phone);
        return pwLogin(user, entity.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PhonePwAuthToken.class.isAssignableFrom(authentication);
    }
}
