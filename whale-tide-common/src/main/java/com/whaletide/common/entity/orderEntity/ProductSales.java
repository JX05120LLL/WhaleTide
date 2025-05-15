package com.whaletide.common.entity.orderEntity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSales {
    private Long id;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer salesCount;
    private BigDecimal salesAmount;
}
