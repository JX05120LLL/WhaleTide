package com.whaletide.admin.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSalesVO {

    private Long id;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer salesCount;
    private BigDecimal salesAmount;
}
