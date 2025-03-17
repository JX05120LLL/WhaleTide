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
//        registry.addInterceptor(jwtInterceptor) //注册拦截器
//                .addPathPatterns("/**") //拦截所有请求
//                .excludePathPatterns("/admin/login", //排除登录请求
//                        "/admin/register", //排除注册请求
//                        "/doc.html", //排除swagger文档
//                        "/webjars/**", //排除webjars资源
//                        "/swagger-resources");  //排除swagger资源
    }
}
