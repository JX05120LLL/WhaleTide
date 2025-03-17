package com.whale_tide.config;

import com.whale_tide.util.HutoolPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new HutoolPasswordEncoder();
    }

    //配置安全拦截器，让所有请求都无需security认证
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll() // 允许所有请求无需认证
                .and()
                .csrf().disable(); // 禁用 CSRF 保护
        return http.build();
    }

}
