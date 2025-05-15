package com.whaletide.client.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/Knife4j配置类
 */
@Configuration("clientKnife4jConfig")
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WhaleTide客户端API")
                        .description("WhaleTide客户端接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("WhaleTide团队")
                                .email("support@whaletide.com"))
                        .license(new License().name("Apache 2.0")));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户接口")
                .pathsToMatch("/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("商品接口")
                .pathsToMatch("/api/product/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("订单接口")
                .pathsToMatch("/api/order/**")
                .build();
    }
} 