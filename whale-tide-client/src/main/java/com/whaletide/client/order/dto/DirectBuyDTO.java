package com.whaletide.client.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 直接购买DTO
 */
@Data
@Schema(description = "直接购买请求")
public class DirectBuyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", required = true)
    private Long productId;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量最小为1")
    @Schema(description = "购买数量", required = true)
    private Integer quantity;

    /**
     * 规格ID
     */
    @Schema(description = "规格ID")
    private Long skuId;
    
    /**
     * 用户ID（前端传入，解决身份识别问题）
     */
    @Schema(description = "用户ID（前端可以传入避免后端无法获取）")
    private Long userId;
} 