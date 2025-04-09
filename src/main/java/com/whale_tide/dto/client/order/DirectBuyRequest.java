package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 直接购买请求DTO
 */
@Data
@ApiModel(description = "直接购买请求")
public class DirectBuyRequest {
    
    @ApiModelProperty(value = "商品ID", required = true)
    private Long productId;
    
    @ApiModelProperty(value = "SKU ID")
    private Long productSkuId;
    
    @ApiModelProperty(value = "购买数量", required = true)
    private Integer quantity;
} 