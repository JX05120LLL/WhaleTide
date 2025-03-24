package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付成功回调响应对象
 */
@Data
@ApiModel(description = "支付成功回调响应")
public class PaySuccessResponse {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
    
    @ApiModelProperty("订单编号")
    private String orderSn;
    
    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;
    
    @ApiModelProperty("订单状态")
    private Integer status;
} 