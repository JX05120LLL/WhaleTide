package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 确认收货请求对象
 */
@Data
@ApiModel(description = "确认收货请求")
public class ConfirmReceiveRequest {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
} 