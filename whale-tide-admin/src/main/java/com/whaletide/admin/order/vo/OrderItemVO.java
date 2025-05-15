package com.whaletide.admin.order.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class OrderItemVO {
    Long id;
    Long productId;
    String productName;
    String productImage;
    BigDecimal price;
    Integer quantity;
}
