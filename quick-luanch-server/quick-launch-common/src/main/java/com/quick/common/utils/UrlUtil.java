package com.quick.common.utils;

import com.quick.common.config.AliyunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author CShisan
 */
@Component
public class UrlUtil {
    private final String urlPrefix;

    @Autowired
    public UrlUtil(AliyunConfig aliyunConfig) {
        this.urlPrefix = aliyunConfig.getOssConfig().getUrlPrefix();
    }

    private static final String DEFAULT_URL = "https://oss.xxx.com/app/blog/images/default-url";
    private static final String URL_PREFIX_REGEX = "^((https|http)?://)\\S+$";

    /**
     * 图片地址兼容处理
     *
     * @see UrlUtil#compatible(String, boolean)
     */
    public String compatible(String url) {
        return compatible(url, false);
    }

    /**
     * 图片地址兼容处理
     *
     * @see UrlUtil#compatible(String, boolean, String)
     */
    public String compatible(String url, boolean isDefault) {
        return compatible(url, isDefault, null);
    }

    /**
     * 图片地址兼容处理
     *
     * @param url          图片地址
     * @param isUseDefault 是否使用默认图片url
     * @param defaultUrl   默认图片url
     * @return url
     */
    public String compatible(String url, boolean isUseDefault, String defaultUrl) {
        if (CheckUtil.isBlank(url)) {
            // 初始化默认url,根据标志位是否使用默认url
            defaultUrl = CheckUtil.isBlank(defaultUrl) ? DEFAULT_URL : defaultUrl;
            url = isUseDefault ? defaultUrl : "";
        } else {
            url = isHasPrefix(url) ? url : urlPrefix.concat(url);
        }
        return url;
    }

    /**
     * 判断url是否有前缀
     *
     * @param url url
     * @return status
     */
    private boolean isHasPrefix(String url) {
        return CheckUtil.nonBlank(url) && url.matches(URL_PREFIX_REGEX);
    }
}
