package com.whaletide.client.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 支付成功DTO（简化版）
 */
@Data
@Schema(description = "支付成功请求（简化版）")
public class PaySuccessDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", required = true)
    private Long orderId;

    /**
     * 支付方式：1-支付宝；2-微信支付；3-银联支付
     */
    @Schema(description = "支付方式：1-支付宝；2-微信支付；3-银联支付")
    private Integer payType = 1;
} 