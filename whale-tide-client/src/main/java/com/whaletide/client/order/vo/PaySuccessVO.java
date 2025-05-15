package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付成功结果VO（简化版）
 */
@Data
@Schema(description = "支付成功结果（简化版）")
public class PaySuccessVO implements Serializable {
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
     * 支付状态：true-成功，false-失败
     */
    @Schema(description = "支付状态：true-成功，false-失败")
    private Boolean success;
} 