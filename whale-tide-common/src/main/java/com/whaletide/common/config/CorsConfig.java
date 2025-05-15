package com.whaletide.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/**
 * 全局跨域配置
 * 优化版本：增加了更多安全配置
 */
@Configuration
public class CorsConfig {

    /**
     * 允许跨域调用的过滤器
     */
    @Bean("customCorsFilter")
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的域名列表，包含各种可能的前端地址
        config.addAllowedOrigin("http://localhost:3939");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://127.0.0.1:3939");
        config.addAllowedOrigin("http://127.0.0.1:8080");
        
        // 允许的HTTP方法
        config.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        );
        
        // 允许的头信息
        config.setAllowedHeaders(Collections.singletonList("*"));
        
        // 暴露的头信息
        config.setExposedHeaders(
                Arrays.asList("Authorization", "X-Total-Count", "Link", "X-Rate-Limit-Remaining")
        );
        
        // 允许跨越发送cookie
        config.setAllowCredentials(true);
        
        // 预检请求的缓存时间（秒）
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口都应用这个配置
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
} 