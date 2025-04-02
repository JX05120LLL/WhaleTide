package com.whale_tide.dto.client.member;

import io.swagger.annotations.ApiModel;
import lombok.Data;
/**
 * 修改密码请求
 */
@Data
@ApiModel("修改密码请求")
public class PasswordUpdateRequest {
    private String oldPassword;

    private String newPassword;
}
