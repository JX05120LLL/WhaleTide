package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品规格VO
 */
@Data
@Schema(description = "商品规格")
public class ProductSkuVO {
    
    @Schema(description = "规格ID")
    private Long id;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "规格编码")
    private String skuCode;
    
    @Schema(description = "规格价格")
    private Double price;
    
    @Schema(description = "规格库存")
    private Integer stock;
    
    @Schema(description = "规格图片")
    private String pic;
    
    @Schema(description = "规格属性")
    private List<SkuAttributeVO> attributes;
    
    /**
     * 规格属性VO
     */
    @Data
    @Schema(description = "规格属性")
    public static class SkuAttributeVO {
        
        @Schema(description = "属性ID")
        private Long attributeId;
        
        @Schema(description = "属性名称")
        private String attributeName;
        
        @Schema(description = "属性值")
        private String attributeValue;
    }
} 