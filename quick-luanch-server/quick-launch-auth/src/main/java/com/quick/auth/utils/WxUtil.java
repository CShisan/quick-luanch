package com.quick.auth.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.quick.auth.entity.WxLoginEntity;
import com.quick.auth.security.exception.AuthException;
import com.quick.common.config.WxConfig;
import com.quick.common.enums.CodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author CShisan
 */
@Component
public class WxUtil {
    private final WxMaService wxMaService;

    @Autowired
    public WxUtil(WxConfig config) {
        // 解密工具注入
        this.wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaDefaultConfig = new WxMaDefaultConfigImpl();
        wxMaDefaultConfig.setAppid(config.getAppId());
        wxMaDefaultConfig.setSecret(config.getSecret());
        this.wxMaService.setWxMaConfig(wxMaDefaultConfig);
    }

    /**
     * 获取微信登录实体
     *
     * @param entity entity
     * @return 完整微信登录实体
     */
    public WxLoginEntity getWxLoginEntity(WxLoginEntity entity) {
        try {
            String code = entity.getCode();
            WxMaUserService userService = wxMaService.getUserService();
            WxMaJscode2SessionResult session = userService.getSessionInfo(code);
            WxMaPhoneNumberInfo phoneNoInfo = userService.getPhoneNoInfo(code);
            entity.setOpenid(session.getOpenid());
            entity.setSessionKey(session.getSessionKey());
            entity.setPhoneNumber(phoneNoInfo.getPhoneNumber());
            entity.setPurePhoneNumber(phoneNoInfo.getPurePhoneNumber());
            entity.setCountryCode(phoneNoInfo.getCountryCode());
            return entity;
        } catch (Exception e) {
            throw new AuthException(CodeEnum.USER_AUTH_ERROR, "微信登录异常", e);
        }
    }
}
