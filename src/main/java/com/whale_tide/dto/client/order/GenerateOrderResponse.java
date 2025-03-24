package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 生成订单响应对象
 */
@Data
@ApiModel(description = "生成订单响应")
public class GenerateOrderResponse {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
    
    @ApiModelProperty("应付金额")
    private BigDecimal payAmount;
    
    @ApiModelProperty("支付方式：0->未支付；1->支付宝；2->微信")
    private Integer payType;
} 