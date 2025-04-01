package com.whale_tide.dto.client.member;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "上传头像响应")
public class AvatarUploadResponse {
    private String url;
}
