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

    @NotBlank(message = "商品名称不能为空")
    @ApiModelProperty(value = "商品名称关键词", required = true)
    private String productName;

    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty(value = "商品ID", required = true)
    private Long productId;
} 