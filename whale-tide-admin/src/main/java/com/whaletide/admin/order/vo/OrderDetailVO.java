package com.whaletide.admin.order.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    // 订单基本信息
    private Long id;
    private String orderSn;
    private Integer status;
    private Double totalAmount;
    private Double payAmount;
    private Double freightAmount;
    private Double discountAmount;
    private Integer payType;
    private String payTypeDesc;
    private String statusDesc;
    private LocalDateTime createTime;
    private LocalDateTime paymentTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime receiveTime;

    // 收货信息
    private String receiverName;
    private String receiverPhone;
    private String receiverProvince;
    private String receiverCity;
    private String receiverRegion;
    private String receiverDetailAddress;
    private String note;

    // 物流信息
    private String deliveryCompany;
    private String deliverySn;

    // 订单商品列表
    private List<OrderItemVO> orderItems;
    
    // 用户信息
    private Long userId;
    private String userName;
    private String userAvatar;
} 