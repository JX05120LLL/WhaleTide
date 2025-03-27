package com.whale_tide.dto.management.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * 获取购物车列表的响应对象
 */
@Data
public class CartListResponse {
    private List<CartItemResponse> cartItems;
    private BigDecimal totalAmount;
    private BigDecimal promotionAmount;
    private BigDecimal payAmount;

    @Data
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private Long productSkuId;
        private String productName;
        private String productPic;
        private BigDecimal price;
        private Integer quantity;
        private String productAttr;
        private Integer checked; // 0->未选中；1->已选中
    }
}
