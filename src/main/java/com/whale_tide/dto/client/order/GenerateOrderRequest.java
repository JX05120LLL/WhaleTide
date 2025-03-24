package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 生成订单请求对象
 */
@Data
@ApiModel(description = "生成订单请求")
public class GenerateOrderRequest {
    
    @ApiModelProperty("收货地址ID")
    private Long addressId;
    
    @ApiModelProperty("优惠券ID")
    private Long couponId;
    
    @ApiModelProperty("支付方式：0->未支付；1->支付宝；2->微信")
    private Integer payType;
    
    @ApiModelProperty("订单备注")
    private String memberMessage;
    
    @ApiModelProperty("购物车项ID列表")
    private List<Long> cartIds;
} 