package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 取消订单请求对象
 */
@Data
@ApiModel(description = "取消订单请求")
public class CancelOrderRequest {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
} 