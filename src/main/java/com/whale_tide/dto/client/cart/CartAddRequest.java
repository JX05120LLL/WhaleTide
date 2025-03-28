package com.whale_tide.dto.client.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 *添加购物车请求参数
 */
@Data
@ApiModel(value = "添加购物车请求参数")
public class CartAddRequest {
    @ApiModelProperty("产品ID")
    private Long productId;
    @ApiModelProperty("产品SKU ID")
    private Long productSkuId;
    @ApiModelProperty("购买数量")
    private Integer quantity;

}
