package com.whale_tide.dto.client.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录请求参数
 * @author Bro_cat
 */


@Data
public class LoginRequest {
    @ApiModelProperty("用户名/手机号")
    private String username;//用户名/手机号
    @ApiModelProperty("密码")
    private String password;//密码
}
