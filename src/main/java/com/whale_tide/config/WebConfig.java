package com.whale_tide.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor) // 注册拦截器
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        // 前台用户登录注册相关接口
                        "/sso/login",
                        "/sso/register",
                        "/sso/sms/code",
                        
                        // 后台管理员登录注册接口
                        "/admin/login",
                        "/admin/register",

                        // 静态资源和文档
                        "/favicon.ico",
                        "/static/**",
                        
                        // Swagger文档相关
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/configuration/ui",
                        "/configuration/security"
                );
    }
}
