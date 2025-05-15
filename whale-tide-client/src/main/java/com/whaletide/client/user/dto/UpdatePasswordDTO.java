package com.whaletide.client.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Schema(description = "修改密码DTO")
public class UpdatePasswordDTO {

    @NotEmpty(message = "旧密码不能为空")
    @Schema(description = "旧密码")
    private String oldPassword;

    @NotEmpty(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;

    @NotEmpty(message = "确认密码不能为空")
    @Schema(description = "确认密码")
    private String confirmPassword;
} 