package com.whaletide.admin.product.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDetailOV {
    Long id;
    String name;
    String description;
    BigDecimal price;
    Integer stock;
    Integer initialStock;
    Integer sales;
    Integer status;
    String category;
    Long categoryId;
    String image;
    String[] images;
    LocalDateTime createTime;
    LocalDateTime updateTime;
}
