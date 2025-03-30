package com.whale_tide.dto.client.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 修改购物车中商品数量的请求参数
 */
@Data
@ApiModel("修改购物车中商品数量的请求参数")
public class CartUpdateQuantityRequest {
    @ApiModelProperty("购物车ID")
    private Long id;
    @ApiModelProperty("商品数量")
    private Integer quantity;
}
