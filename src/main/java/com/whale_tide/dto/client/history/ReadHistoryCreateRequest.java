package com.whale_tide.dto.client.history;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加搜索历史记录请求参数
 */
@Data
@ApiModel("添加搜索历史记录请求参数")
public class ReadHistoryCreateRequest {
    
    @NotBlank(message = "搜索关键词不能为空")
    @ApiModelProperty(value = "搜索关键词", required = true)
    private String keyword;
} 