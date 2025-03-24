package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付成功回调请求对象
 */
@Data
@ApiModel(description = "支付成功回调请求")
public class PaySuccessRequest {
    
    @ApiModelProperty("订单ID")
    private Long orderId;
    
    @ApiModelProperty("支付方式：1->支付宝；2->微信")
    private Integer payType;
} 