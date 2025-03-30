package com.whale_tide.dto.client.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 删除购物车请求参数
 */
@Data
@ApiModel("删除购物车请求参数")
public class CartDeleteRequest {
    @ApiModelProperty("购物车ID")
    private String ids;
}
