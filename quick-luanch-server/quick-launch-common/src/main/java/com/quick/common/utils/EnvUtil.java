package com.quick.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yuanbai
 */
@Component
public class EnvUtil {
    private static final String PROD = "prod";
    private static final String TEST = "test";
    private static final String DEV = "dev";
    private static final String SELF = "self";
    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 生产环境执行回调方法
     *
     * @param prod prod
     */
    public void executeProd(EnvCallback prod) {
        execute(prod, null, null);
    }

    /**
     * 开发环境执行回调方法
     *
     * @param dev dev
     */
    public void executeDev(EnvCallback dev) {
        execute(null, dev, null);
    }

    /**
     * 执行回调方法
     *
     * @see EnvUtil#execute(EnvCallback)
     */
    public void execute(EnvCallback prod, EnvCallback dev, EnvCallback test) {
        if (PROD.equalsIgnoreCase(env)) {
            execute(prod);
        } else if (this.isDev()) {
            execute(dev);
        } else if (TEST.equalsIgnoreCase(env)) {
            execute(test);
        }
    }

    /**
     * 执行回调方法
     *
     * @param callback callback
     */
    private void execute(EnvCallback callback) {
        if (CheckUtil.nonNull(callback)) {
            callback.callback();
        }
    }

    /**
     * 判断是否为生产环境
     *
     * @return status
     */
    private boolean isDev() {
        return DEV.equalsIgnoreCase(env) || SELF.equalsIgnoreCase(env);
    }

    @FunctionalInterface
    public interface EnvCallback {
        /**
         * 环境工具回调接口
         */
        void callback();
    }
}
