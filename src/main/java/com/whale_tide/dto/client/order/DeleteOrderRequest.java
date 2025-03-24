package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 删除订单请求对象
 */
@Data
@ApiModel(description = "删除订单请求")
public class DeleteOrderRequest {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
} 