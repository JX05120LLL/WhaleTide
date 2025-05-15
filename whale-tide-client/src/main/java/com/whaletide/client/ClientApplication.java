package com.whaletide.client;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 客户端启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.whaletide.common", "com.whaletide.client"})
@MapperScan(basePackages = {"com.whaletide.common.mapper"})
public class ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
