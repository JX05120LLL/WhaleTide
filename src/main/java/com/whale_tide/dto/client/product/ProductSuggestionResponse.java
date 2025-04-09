package com.whale_tide.dto.client.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品搜索建议响应对象
 */
@Data
@ApiModel("商品搜索建议响应对象")
public class ProductSuggestionResponse {
    
    @ApiModelProperty("商品ID")
    private Long id;
    
    @ApiModelProperty("商品名称")
    private String name;
} 