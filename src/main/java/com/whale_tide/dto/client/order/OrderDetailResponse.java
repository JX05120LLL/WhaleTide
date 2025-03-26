package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情响应对象
 */
@Data
@ApiModel(description = "订单详情响应")
public class OrderDetailResponse {
    
    @ApiModelProperty("订单ID")
    private Long id;
    
    @ApiModelProperty("订单编号")
    private String orderSn;
    
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    @ApiModelProperty("应付金额")
    private BigDecimal payAmount;
    
    @ApiModelProperty("订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;
    
    @ApiModelProperty("订单备注")
    private String memberMessage;
    
    @ApiModelProperty("支付方式：0->未支付；1->支付宝；2->微信")
    private Integer payType;
    
    @ApiModelProperty("收货人姓名")
    private String receiverName;
    
    @ApiModelProperty("收货人电话")
    private String receiverPhone;
    
    @ApiModelProperty("收货地址")
    private String receiverAddress;
    
    @ApiModelProperty("订单项列表")
    private List<OrderItemResponse> orderItemList;
    
    /**
     * 订单项响应
     */
    @Data
    public static class OrderItemResponse {
        @ApiModelProperty("订单项ID")
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
        
        @ApiModelProperty("商品属性")
        private String productAttr;
    }
} 