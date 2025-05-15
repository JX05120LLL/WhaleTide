package com.whaletide.admin.product.dto;

import lombok.Data;

@Data
public class ProductListDTO {
    Long page;
    Long pageSize;
    String keyword;
    Integer status;
}
