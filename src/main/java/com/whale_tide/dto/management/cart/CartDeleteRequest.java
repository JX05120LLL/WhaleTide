package com.whale_tide.dto.management.cart;

import lombok.Data;

/**
 * 删除购物车请求参数
 */
@Data
public class CartDeleteRequest {
    private String ids;
}
