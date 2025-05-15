package com.whaletide.client.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应VO
 */
@Data
@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "令牌前缀")
    private String tokenHead;

    @Schema(description = "过期时间(秒)")
    private Long expiresIn;
} 