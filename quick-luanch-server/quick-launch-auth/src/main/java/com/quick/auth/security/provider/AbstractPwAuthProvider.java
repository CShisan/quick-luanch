package com.quick.auth.security.provider;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.security.datasource.UserDetailsService;
import com.quick.auth.utils.PwdEncoderUtil;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.BeanUtil;
import com.quick.domain.dao.UserDao;
import com.quick.domain.entity.User;

/**
 * @author yuanbai
 */
public abstract class AbstractPwAuthProvider extends AbstractAuthProvider {
    public AbstractPwAuthProvider(UserDetailsService userDetailsService, UserDao userDao) {
        super(userDetailsService, userDao);
    }

    /**
     * 统一密码登录
     *
     * @param req      请求参数
     * @param password 密码
     * @return 登录实体
     */
    protected LoginEntity pwLogin(User req, String password) {
        // 查询用户
        User user = userDao.getOneBy(req);
        ExceptionHandler.requireNotNull(user);

        // 校验密码
        LoginEntity source = BeanUtil.convert(user, LoginEntity::new);
        String sourcePw = source.getPassword();
        boolean isEqualPw = PwdEncoderUtil.matches(password, sourcePw);
        ExceptionHandler.assertTrue(isEqualPw, CodeEnum.USER_AUTH_ERROR, "密码错误");

        return source;
    }
}
