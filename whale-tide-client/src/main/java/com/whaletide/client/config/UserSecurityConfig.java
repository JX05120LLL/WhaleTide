package com.whaletide.client.config;

import com.whaletide.client.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户安全配置
 * 确保系统使用自定义的UserDetailsService实现
 */
@Configuration
@Order(1) // 确保这个配置优先于其他配置
public class UserSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    /**
     * 提供UserDetailsService的主Bean
     * 使用@Primary确保该实现被优先注入到需要UserDetailsService的地方
     */
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }
} 