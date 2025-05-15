package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品VO - 用于品牌相关产品展示
 */
@Data
@Schema(description = "商品信息")
public class ProductVO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "商品副标题")
    private String subTitle;
    
    @Schema(description = "商品主图")
    private String pic;
    
    @Schema(description = "商品价格")
    private Double price;
    
    @Schema(description = "商品原价")
    private Double originalPrice;
    
    @Schema(description = "销售数量")
    private Integer saleCount;
    
    @Schema(description = "库存")
    private Integer stock;
    
    @Schema(description = "品牌ID")
    private Long brandId;
    
    @Schema(description = "分类ID")
    private Long categoryId;
} 