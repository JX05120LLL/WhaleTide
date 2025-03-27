package com.whale_tide.dto.management.cart;

import lombok.Data;
/**
 *添加购物车请求参数
 */
@Data
public class CartAddRequest {
    private Long productId;
    private Long productSkuId;
    private Integer quantity;

}
