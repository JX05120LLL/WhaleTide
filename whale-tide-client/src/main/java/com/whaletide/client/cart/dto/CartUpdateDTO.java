package com.whaletide.client.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 购物车更新DTO
 */
@Data
@Schema(description = "购物车更新请求")
public class CartUpdateDTO {
    
    @Schema(description = "购物车项ID", required = true)
    @NotNull(message = "购物车项ID不能为空")
    private Long id;
    
    @Schema(description = "商品数量")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
    
    @Schema(description = "是否选中：0-否，1-是")
    private Integer checked;
} 