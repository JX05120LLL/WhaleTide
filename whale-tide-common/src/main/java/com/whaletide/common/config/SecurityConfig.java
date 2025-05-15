package com.whaletide.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security安全配置
 * 增加了方法级别的安全控制和更多安全设置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * 密码编码器
     * 使用BCrypt强哈希算法替代自定义的密码编码器，提高安全性
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置安全拦截器
     * 基础安全配置，由子模块通过@Order覆盖特定路径的安全规则
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用基本认证
            .httpBasic().disable()
            // 禁用表单登录
            .formLogin().disable()
            // 禁用csrf，因为使用JWT方式
            .csrf().disable()
            // 禁用session
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            // 设置异常处理
            .exceptionHandling()
                .and()
            // 配置权限
            .authorizeRequests()
                // 设置允许匿名访问的路径
                .antMatchers(
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/v3/api-docs/**",
                    "/doc.html",
                    "/webjars/**",
                    "/favicon.ico",
                    "/error"
                ).permitAll()
                // 其他所有请求都需要认证
                .anyRequest().permitAll();  // 这里暂时允许所有请求，实际JWT认证在拦截器中处理
            
        return http.build();
    }
} 