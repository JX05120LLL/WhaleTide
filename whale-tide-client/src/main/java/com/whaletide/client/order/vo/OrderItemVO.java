package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单项VO
 */
@Data
@Schema(description = "订单项信息")
public class OrderItemVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 订单项ID
     */
    @Schema(description = "订单项ID")
    private Long id;
    
    /**
     * 商品ID
     */
    @Schema(description = "商品ID")
    private Long productId;
    
    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    private String productName;
    
    /**
     * 商品图片
     */
    @Schema(description = "商品图片")
    private String productPic;
    
    /**
     * 商品单价
     */
    @Schema(description = "商品单价")
    private Double price;
    
    /**
     * 购买数量
     */
    @Schema(description = "购买数量")
    private Integer quantity;
    
    /**
     * 小计金额
     */
    @Schema(description = "小计金额")
    private Double subtotal;
} 