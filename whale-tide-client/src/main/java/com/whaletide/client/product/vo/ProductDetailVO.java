package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品详情VO
 */
@Data
@Schema(description = "商品详情")
public class ProductDetailVO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "商品价格")
    private Double price;
    
    @Schema(description = "库存")
    private Integer stock;
    
    @Schema(description = "商品主图")
    private String pic;
    
    @Schema(description = "商品主图(兼容前端)")
    private String image;
    
    @Schema(description = "商品图册")
    private List<String> albumPics;
    
    @Schema(description = "商品详情")
    private String description;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "品牌ID")
    private Long brandId;
    
    @Schema(description = "品牌名称")
    private String brandName;
    
    @Schema(description = "商品属性")
    private List<ProductAttributeVO> attributes;
    
    @Schema(description = "商品参数")
    private List<ProductParamVO> params;
    
    @Schema(description = "商品规格")
    private List<ProductSkuVO> skus;
    
    // 设置pic的同时也设置image
    public void setPic(String pic) {
        this.pic = pic;
        this.image = pic;
    }
} 