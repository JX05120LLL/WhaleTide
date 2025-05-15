package com.whaletide.client.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 取消订单DTO
 */
@Data
@Schema(description = "取消订单请求")
public class CancelOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", required = true)
    private Long orderId;

    /**
     * 取消原因
     */
    @Schema(description = "取消原因")
    private String reason;
} 