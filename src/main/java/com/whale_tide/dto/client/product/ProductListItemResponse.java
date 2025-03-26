package com.whale_tide.dto.client.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品搜索响应参数
 */

@ApiModel(description = "商品搜索响应")
@Data
public class ProductListItemResponse {

    @ApiModelProperty("商品ID")
    private Long id;
    @ApiModelProperty("商品名称")
    private String name;
    @ApiModelProperty("商品图片")
    private String pic;
    @ApiModelProperty("商品价格")
    private BigDecimal price;
    @ApiModelProperty("商品销量")
    private Integer sale;

}
