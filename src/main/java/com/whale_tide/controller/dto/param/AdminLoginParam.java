package com.whale_tide.controller.dto.param;

import lombok.Data;

/**
 * AdminLogin接口请求参数
 */
@Data
public class AdminLoginParam {
    private String username;
    private String password;
}
