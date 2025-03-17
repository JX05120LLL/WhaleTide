package com.whale_tide.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取token相关配置
 */
@Component
@ConfigurationProperties(prefix = "token")
@Data
public class TokenConfig {
    private Integer expireHours;
    private String secret;
}
