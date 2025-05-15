package com.whaletide.client.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户注册DTO
 */
@Data
@Schema(description = "用户注册请求")
public class UserRegisterDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", required = true)
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Schema(description = "密码", required = true)
    private String password;
    
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;
} 