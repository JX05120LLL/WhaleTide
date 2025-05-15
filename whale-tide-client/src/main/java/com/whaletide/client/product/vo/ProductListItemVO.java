package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品列表项VO
 */
@Data
@Schema(description = "商品列表项")
public class ProductListItemVO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品价格")
    private Double price;
    
    @Schema(description = "商品图片")
    private String pic;
    
    @Schema(description = "商品图片(兼容前端)")
    private String image;

    @Schema(description = "库存")
    private Integer stock;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "品牌ID")
    private Long brandId;
    
    @Schema(description = "品牌名称")
    private String brandName;
    
    @Schema(description = "销量")
    private Integer soldCount;
    
    // 设置pic的同时也设置image
    public void setPic(String pic) {
        this.pic = pic;
        this.image = pic;
    }
} 