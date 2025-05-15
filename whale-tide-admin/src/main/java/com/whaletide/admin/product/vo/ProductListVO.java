package com.whaletide.admin.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductListVO {
    Long total;
    List<ProductListItemVO> list;
}
