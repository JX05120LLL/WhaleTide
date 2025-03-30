package com.whale_tide.dto.client.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * 获取购物车列表的响应对象
 */
@Data
@ApiModel("获取购物车列表的响应对象")
public class CartListResponse {
    @ApiModelProperty("购物车列表")
    private List<CartItemResponse> cartItems;
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty("促销金额")
    private BigDecimal promotionAmount;
    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;

    @Data
    public static class CartItemResponse {
        @ApiModelProperty("购物车项ID")
        private Long id;
        @ApiModelProperty("购物车项的商品ID")
        private Long productId;
        @ApiModelProperty("购物车项的商品SKU ID")
        private Long productSkuId;
        @ApiModelProperty("购物车项的商品名称")
        private String productName;
        @ApiModelProperty("购物车项的商品图片")
        private String productPic;
        @ApiModelProperty("购物车项的商品价格")
        private BigDecimal price;
        @ApiModelProperty("购物车项的商品数量")
        private Integer quantity;
        @ApiModelProperty("购物车项的商品属性")
        private String productAttr;
        @ApiModelProperty("购物车项是否选中")
        private Integer checked; // 0->未选中；1->已选中
    }
}
