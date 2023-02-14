package com.quick.file.provider;

import com.quick.common.config.AliyunConfig;

/**
 * @author CShisan
 */
public abstract class AbstractOssUploadProvider extends AbstractContainerUploadProvider {
    /**
     * COS配置
     *
     * @return config
     */
    protected abstract AliyunConfig.OssConfig config();

    @Override
    public String pathPrefix() {
        return config().getUrlPrefix();
    }

    @Override
    public String bucketBasePath() {
        return config().getBucketBasePath();
    }

}
