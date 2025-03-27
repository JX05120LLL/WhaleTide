package com.whale_tide.dto.management.cart;

import lombok.Data;
/**
 * 修改购物车中商品选中状态的请求参数
 */
@Data
public class CartUpdateCheckedRequest {
    private String ids;
    private Integer checked;
}
