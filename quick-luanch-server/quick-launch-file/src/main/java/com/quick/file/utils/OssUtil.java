package com.quick.file.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.quick.common.config.AliyunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Optional;

/**
 * @author CShisan
 */
@Component
public class OssUtil {
    private final AliyunConfig.OssConfig config;
    private final OSS client;

    @Autowired
    public OssUtil(AliyunConfig aliyunConfig) {
        // 创建
        this.config = aliyunConfig.getOssConfig();
        String accessKeyId = aliyunConfig.getAccessKeyId();
        String accessKeySecret = aliyunConfig.getAccessKeySecret();
        DefaultCredentialProvider provider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
        this.client = OSSClientBuilder.create().endpoint(config.getEndpoint()).credentialsProvider(provider).build();
    }

    public OSS client() {
        return client;
    }

    public AliyunConfig.OssConfig config() {
        return config;
    }

    @PreDestroy
    public void destroy() {
        Optional.ofNullable(client).ifPresent(OSS::shutdown);
    }
}
