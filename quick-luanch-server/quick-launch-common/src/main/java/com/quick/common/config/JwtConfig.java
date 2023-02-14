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
@ConfigurationProperties(prefix = "jwt")
@PropertySource(value = "classpath:config/jwt.yml", factory = ConfigurationSourceFactory.class)
public class JwtConfig {
    private String type;
    private String header;
    private String secret;
    private Long tokenExp;
    private Long refreshTokenExp;
    private String tokenKey;
    private String refreshTokenKey;
    private String tokenValuePrefix;
}
