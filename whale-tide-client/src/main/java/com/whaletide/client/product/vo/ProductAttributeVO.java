package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品属性VO
 */
@Data
@Schema(description = "商品属性")
public class ProductAttributeVO {
    
    @Schema(description = "属性ID")
    private Long id;
    
    @Schema(description = "属性名称")
    private String name;
    
    @Schema(description = "属性值")
    private String value;
} 