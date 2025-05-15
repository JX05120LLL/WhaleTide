package com.whaletide.admin.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductListItemVO {
    Long id;
    String name;
    Long categoryId;
    String categoryName;
    BigDecimal price;
    BigDecimal originalPrice;
    String coverImage;
    Integer sales;
    Integer stock;
    Integer status;
    LocalDateTime createTime;
    LocalDateTime updateTime;
}
