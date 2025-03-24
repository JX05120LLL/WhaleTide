package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 确认订单响应对象
 */
@Data
@ApiModel(description = "确认订单响应")
public class ConfirmOrderResponse {
    
    @ApiModelProperty("购物车项列表")
    private List<CartItemResponse> cartItems;
    
    @ApiModelProperty("收货地址列表")
    private List<AddressResponse> memberReceiveAddressList;
    
    @ApiModelProperty("优惠券列表")
    private List<CouponResponse> couponList;
    
    @ApiModelProperty("计算金额")
    private CalcAmountResponse calcAmount;
    
    /**
     * 购物车项响应
     */
    @Data
    public static class CartItemResponse {
        @ApiModelProperty("购物车项ID")
        private Long id;
        
        @ApiModelProperty("商品ID")
        private Long productId;
        
        @ApiModelProperty("商品名称")
        private String productName;
        
        @ApiModelProperty("商品图片")
        private String productPic;
        
        @ApiModelProperty("商品价格")
        private BigDecimal price;
        
        @ApiModelProperty("商品数量")
        private Integer quantity;
        
        @ApiModelProperty("商品属性")
        private String productAttr;
    }
    
    /**
     * 收货地址响应
     */
    @Data
    public static class AddressResponse {
        @ApiModelProperty("地址ID")
        private Long id;
        
        @ApiModelProperty("收货人姓名")
        private String name;
        
        @ApiModelProperty("收货人电话")
        private String phoneNumber;
        
        @ApiModelProperty("是否默认地址：0->否；1->是")
        private Integer defaultStatus;
        
        @ApiModelProperty("省份")
        private String province;
        
        @ApiModelProperty("城市")
        private String city;
        
        @ApiModelProperty("区域")
        private String district;
        
        @ApiModelProperty("详细地址")
        private String detailAddress;
    }
    
    /**
     * 优惠券响应
     */
    @Data
    public static class CouponResponse {
        @ApiModelProperty("优惠券ID")
        private Long id;
        
        @ApiModelProperty("优惠券名称")
        private String name;
        
        @ApiModelProperty("优惠金额")
        private BigDecimal amount;
        
        @ApiModelProperty("使用门槛")
        private BigDecimal minPoint;
        
        @ApiModelProperty("开始时间")
        private Date startTime;
        
        @ApiModelProperty("结束时间")
        private Date endTime;
        
        @ApiModelProperty("使用类型：0->全场通用；1->指定分类；2->指定商品")
        private Integer useType;
    }
    
    /**
     * 计算金额响应
     */
    @Data
    public static class CalcAmountResponse {
        @ApiModelProperty("商品总金额")
        private BigDecimal totalAmount;
        
        @ApiModelProperty("运费")
        private BigDecimal freightAmount;
        
        @ApiModelProperty("促销优惠")
        private BigDecimal promotionAmount;
        
        @ApiModelProperty("优惠券优惠")
        private BigDecimal couponAmount;
        
        @ApiModelProperty("应付金额")
        private BigDecimal payAmount;
    }
} 