package com.whaletide.client.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 购物车添加DTO
 */
@Data
@Schema(description = "购物车添加请求")
public class CartAddDTO {
    
    @Schema(description = "商品ID", required = true)
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @Schema(description = "商品数量", required = true)
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
} 