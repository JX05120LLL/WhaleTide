package com.whale_tide.dto.order;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String skuCode;
    private String skuName;
    private String skuImage;
    private BigDecimal skuPrice;
    private String properties;
} 