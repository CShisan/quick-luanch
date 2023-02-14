package com.quick.auth.security.provider;

import com.quick.auth.entity.LoginEntity;
import com.quick.auth.entity.WxLoginEntity;
import com.quick.auth.security.datasource.UserDetailsService;
import com.quick.auth.security.token.WxAppletAuthToken;
import com.quick.auth.utils.PwdEncoderUtil;
import com.quick.auth.utils.WxUtil;
import com.quick.common.config.ServiceConfig;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.BeanUtil;
import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.IDUtil;
import com.quick.domain.dao.UserDao;
import com.quick.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author yuanbai
 */
@Component
public class WxAppletAuthProvider extends AbstractAuthProvider {
    private final WxUtil wxUtil;
    private final ServiceConfig serviceConfig;

    @Autowired
    public WxAppletAuthProvider(
            UserDetailsService userDetailsService,
            UserDao userDao,
            WxUtil wxUtil,
            ServiceConfig serviceConfig
    ) {
        super(userDetailsService, userDao);
        this.wxUtil = wxUtil;
        this.serviceConfig = serviceConfig;
    }

    @Override
    protected LoginEntity login(Authentication authentication) {
        Assert.isInstanceOf(WxAppletAuthToken.class, authentication, "不支持该认证类型");
        WxLoginEntity entity = ((WxAppletAuthToken) authentication).getPrincipal();
        ExceptionHandler.requireNotNull(entity);

        entity = wxUtil.getWxLoginEntity(entity);
        ExceptionHandler.requireNotNull(entity, CodeEnum.USER_AUTH_ERROR, "登录异常");
        String openid = entity.getOpenid();
        ExceptionHandler.requireNotBlank(openid, CodeEnum.USER_AUTH_ERROR, "登录异常");

        // 查询user
        User user = new User();
        user.setOpenId(openid);
        User sourceUser = userDao.getOneBy(user);

        // 查询user为空则进行注册
        if (CheckUtil.isNull(sourceUser)) {
            this.register(user, entity);
            sourceUser = userDao.getOneBy(user);
        }

        return BeanUtil.convert(sourceUser, entity);
    }

    /**
     * 微信openId注册
     *
     * @param user user
     */
    private void register(User user, WxLoginEntity entity) {
        String uuid = String.valueOf(UUID.randomUUID()).replace("-", "");
        user.setUid(IDUtil.uid());
        user.setUsername(WxLoginEntity.getUsername(uuid));
        user.setPhone(entity.getPhoneNumber());
        user.setPassword(PwdEncoderUtil.encodePwd(entity.getOpenid()));
        user.setAvatar(serviceConfig.getAvatarUrl());
        int row = userDao.save(user);
        ExceptionHandler.operateDbSingleOk(row);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WxAppletAuthToken.class.isAssignableFrom(authentication);
    }
}
