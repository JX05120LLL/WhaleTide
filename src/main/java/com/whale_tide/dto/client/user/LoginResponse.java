package com.whale_tide.dto.client.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录响应参数
 */

@Data
public class LoginResponse {
    @ApiModelProperty("用户token")
    private String token;// 登录成功后返回的token
    @ApiModelProperty("token的头部")
    private String tokenHead; // token的头部

}
