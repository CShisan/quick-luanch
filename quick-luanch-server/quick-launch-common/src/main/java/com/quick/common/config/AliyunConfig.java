package com.quick.common.config;

import com.quick.common.factory.ConfigurationSourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author CShisan
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun")
@PropertySource(value = "classpath:config/aliyun.yml", factory = ConfigurationSourceFactory.class)
public class AliyunConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private final OssConfig ossConfig;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "aliyun.oss")
    public static class OssConfig {
        private String endpoint;
        private String bucketName;
        private String bucketBasePath;
        private String urlPrefix;
    }
}
