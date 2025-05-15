package com.whaletide.client.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息响应VO
 */
@Data
@Schema(description = "用户信息响应")
public class UserInfoResponse {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String icon;

    @Schema(description = "积分")
    private Integer integration;

    @Schema(description = "手机号")
    private String mobile;
    
    @Schema(description = "邮箱")
    private String email;
} 