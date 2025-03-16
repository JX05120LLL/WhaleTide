package com.whale_tide;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whale_tide.mapper")
public class WhaleTideApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleTideApplication.class, args);
    }
}
