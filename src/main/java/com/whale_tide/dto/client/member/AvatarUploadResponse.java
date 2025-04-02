package com.whale_tide.dto.client.member;

import io.swagger.annotations.ApiModel;
import lombok.Data;
/**
 * 上传用户头像响应
 */
@Data
@ApiModel(description = "上传头像响应")
public class AvatarUploadResponse {
    private String url;
}
