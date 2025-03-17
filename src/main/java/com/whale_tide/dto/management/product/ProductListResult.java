package com.whale_tide.dto.management.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/*
  产品列表查询结果
 */
@Data
public class ProductListResult {
    @ApiModelProperty("产品ID")
    private Long id;

    @ApiModelProperty("产品名称")
    private String name;

    @ApiModelProperty("产品图片")
    private String pic;

    @ApiModelProperty("产品价格")
    private BigDecimal price;

    @ApiModelProperty("产品销量")
    private Integer sale;

    @ApiModelProperty("产品品牌名")
    private String brandName;

    @ApiModelProperty("产品分类名")
    private String productCategoryName;

    @ApiModelProperty("上架状态:0-下架，1-上架")
    private Integer publishStatus;

    @ApiModelProperty("新品状态：0-非新品，1-新品")
    private Integer newStatus;

    @ApiModelProperty("推荐状态：0-不推荐，1-推荐")
    private Integer recommendStatus;



}
