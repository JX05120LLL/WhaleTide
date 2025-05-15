package com.whaletide.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 客户端Web MVC配置
 */
@Configuration
public class ClientWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        
        // Swagger UI资源映射
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
        // Knife4j资源映射
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许跨域请求的路径
        registry.addMapping("/**")
                // 允许跨域请求的域名，包含各种可能的前端地址
                .allowedOrigins(
                    "http://localhost:3939",
                    "http://localhost:8080",
                    "http://127.0.0.1:3939",
                    "http://127.0.0.1:8080",
                    "http://localhost:9528"
                )
                // 是否允许证书（cookies）
                .allowCredentials(true)
                // 允许的方法
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
                // 允许的头信息
                .allowedHeaders("*")
                // 预检请求的缓存时间（秒）
                .maxAge(3600)
                // 暴露的头信息
                .exposedHeaders("Authorization");
    }
} 