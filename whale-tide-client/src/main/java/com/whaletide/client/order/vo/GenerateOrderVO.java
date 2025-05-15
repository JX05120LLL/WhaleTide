package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 生成订单结果VO
 */
@Data
@Schema(description = "生成订单结果")
public class GenerateOrderVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long orderId;
    
    /**
     * 订单编号
     */
    @Schema(description = "订单编号")
    private String orderSn;
    
    /**
     * 应付金额
     */
    @Schema(description = "应付金额")
    private Double payAmount;
    
    @Schema(description = "支付方式：1->支付宝；2->微信")
    private Integer payType;
} 