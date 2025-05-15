package com.whaletide.admin.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateDTO {
    Long id;
    String name;
    String description;
    Long categoryId;
    BigDecimal price;
    Integer stock;
    Integer status;
}
