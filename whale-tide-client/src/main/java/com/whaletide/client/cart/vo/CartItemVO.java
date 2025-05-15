package com.whaletide.client.cart.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车项VO
 */
@Data
@Schema(description = "购物车项响应")
public class CartItemVO {
    
    @Schema(description = "购物车项ID")
    private Long id;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品图片")
    private String productImage;
    
    @Schema(description = "规格属性")
    private String skuSpecs;
    
    @Schema(description = "商品单价")
    private BigDecimal price;
    
    @Schema(description = "商品数量")
    private Integer quantity;
    
    @Schema(description = "小计金额")
    private BigDecimal subtotal;
    
    @Schema(description = "是否选中：0-否，1-是")
    private Integer checked;
    
    @Schema(description = "添加时间")
    private LocalDateTime createTime;
} 