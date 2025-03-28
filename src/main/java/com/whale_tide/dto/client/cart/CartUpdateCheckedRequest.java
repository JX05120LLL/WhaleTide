package com.whale_tide.dto.client.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 修改购物车中商品选中状态的请求参数
 */
@Data
@ApiModel("修改购物车中商品选中状态的请求参数")
public class CartUpdateCheckedRequest {
    @ApiModelProperty("购物车ID")
    private String ids;
    @ApiModelProperty("是否选中")
    private Integer checked;
}
