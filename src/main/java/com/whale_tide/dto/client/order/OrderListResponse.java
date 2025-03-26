package com.whale_tide.dto.client.order;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderListResponse {

    @ApiModelProperty("订单ID")
    private Long id;
    @ApiModelProperty("订单号")
    private String orderSn;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;
    @ApiModelProperty("订单状态")
    private Integer status;
    @ApiModelProperty("订单商品列表")
    private List<OrderItemResponse> orderItemList;

    @Data
    public static class OrderItemResponse {
        @ApiModelProperty("订单ID")
        private Long id;
        @ApiModelProperty("商品ID")
        private Long productId;
        @ApiModelProperty("商品名称")
        private String productName;
        @ApiModelProperty("商品图片")
        private String productPic;
        @ApiModelProperty("商品价格")
        private BigDecimal productPrice;
        @ApiModelProperty("商品数量")
        private Integer productQuantity;
        @ApiModelProperty("商品规格")
        private String productAttr;
    }


}
