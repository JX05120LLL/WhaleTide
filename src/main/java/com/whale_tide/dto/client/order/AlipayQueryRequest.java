package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付宝订单查询请求对象
 */
@Data
@ApiModel(description = "支付宝订单查询请求")
public class AlipayQueryRequest {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
} 