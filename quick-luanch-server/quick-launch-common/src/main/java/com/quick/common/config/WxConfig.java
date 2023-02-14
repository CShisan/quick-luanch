package com.quick.common.config;

import com.quick.common.factory.ConfigurationSourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author yuanbai
 */
@Data
@Configuration
@ConfigurationProperties("wx")
@PropertySource(value = "classpath:config/wx.yml", factory = ConfigurationSourceFactory.class)
public class WxConfig {
    private String appId;
    private String secret;
}
