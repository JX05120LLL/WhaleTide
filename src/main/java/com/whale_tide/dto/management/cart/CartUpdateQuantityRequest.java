package com.whale_tide.dto.management.cart;

import lombok.Data;
/**
 * 修改购物车中商品数量的请求参数
 */
@Data
public class CartUpdateQuantityRequest {
    private Long id;
    private Integer quantity;
}
