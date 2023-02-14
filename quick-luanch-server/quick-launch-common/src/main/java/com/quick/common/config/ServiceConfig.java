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
@ConfigurationProperties(prefix = "service")
@PropertySource(value = "classpath:config/service.yml", factory = ConfigurationSourceFactory.class)
public class ServiceConfig {
    private String avatarUrl;
}
