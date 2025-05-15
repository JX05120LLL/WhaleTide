package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品参数VO
 */
@Data
@Schema(description = "商品参数")
public class ProductParamVO {
    
    @Schema(description = "参数ID")
    private Long id;
    
    @Schema(description = "参数名称")
    private String name;
    
    @Schema(description = "参数值")
    private String value;
} 