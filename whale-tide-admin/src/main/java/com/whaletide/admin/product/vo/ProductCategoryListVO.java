package com.whaletide.admin.product.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductCategoryListVO {
    Long id;
    String name;
    String description;
    Integer sort;
    LocalDateTime createTime;
    LocalDateTime updateTime;
}
