package com.whaletide.common.config;

import com.whaletide.common.security.JwtAuthenticationFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JWT安全配置
 * 整合所有JWT相关设置
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtSecurityConfig {
    
    private String secret; // JWT密钥
    private long expiration; // 过期时间（秒）
    private String tokenHeader; // 请求头名称
    private String tokenPrefix; // 令牌前缀（Bearer）
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public JwtSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    /**
     * 配置JWT安全过滤器链
     */
    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        // 添加JWT过滤器，应在用户名密码认证过滤器之前
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // 配置自定义的JWT异常处理器
        http.exceptionHandling(exceptionHandling -> {
            // 可以在这里配置自定义的认证入口点和访问拒绝处理器
        });
        
        // 禁用CSRF，因为使用JWT
        http.csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }
} 