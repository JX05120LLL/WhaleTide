package com.whale_tide.dto.client.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册请求参数
 */

@Data
public class RegisterRequest {

    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("密码")
    private String password;

}
