package com.whaletide.admin.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductAddDTO {
    String name;
    String description;
    BigDecimal price;
    Integer stock;
    Long categoryId;
    String image;
    String[] images;
    Integer status;
}
