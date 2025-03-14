package com.whale_tide.dto.admin;

import lombok.Data;

/**
 * AdminLogin接口请求参数
 */
@Data
public class AdminLoginParam {
    private String username;
    private String password;
}
