package com.whaletide.client.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户登录DTO
 */
@Data
@Schema(description = "用户登录请求")
public class UserLoginDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", required = true)
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", required = true)
    private String password;
} 