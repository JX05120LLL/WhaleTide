package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付宝订单查询响应对象
 */
@Data
@ApiModel(description = "支付宝订单查询响应")
public class AlipayQueryResponse {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
    
    @ApiModelProperty("订单编号")
    private String orderSn;
    
    @ApiModelProperty("订单状态")
    private Integer status;
    
    @ApiModelProperty("交易状态：WAIT_BUYER_PAY/TRADE_CLOSED/TRADE_SUCCESS/TRADE_FINISHED")
    private String tradeStatus;
    
    @ApiModelProperty("支付宝交易号")
    private String tradeNo;
} 