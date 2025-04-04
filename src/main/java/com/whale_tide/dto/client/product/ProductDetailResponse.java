package com.whale_tide.dto.client.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情响应对象
 */
@Data
@ApiModel("商品详情响应")
public class ProductDetailResponse {
    @ApiModelProperty("商品ID")
    private Long id;
    @ApiModelProperty("商品名称")
    private String name;
    @ApiModelProperty("商品图片")
    private String pic;
    @ApiModelProperty("商品价格")
    private BigDecimal price;
    @ApiModelProperty("原价")
    private BigDecimal originalPrice;
    @ApiModelProperty("商品销量")
    private Integer sale;
    @ApiModelProperty("商品库存")
    private Integer stock;
    @ApiModelProperty("品牌信息")
    private BrandInfo brand;
    @ApiModelProperty("商品分类ID")
    private String categoryName;
    @ApiModelProperty("商品属性")
    private List<ProductAttribute> productAttributeList;
    @ApiModelProperty("商品描述")
    private String description;

    // 内部类
    @Data
    @AllArgsConstructor
    public static class BrandInfo {
        @ApiModelProperty("品牌ID")
        private Long id;
        @ApiModelProperty("品牌名称")
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class ProductAttribute {
        @ApiModelProperty("属性ID")
        private String name;
        @ApiModelProperty("属性值")
        private String value;
    }
}
