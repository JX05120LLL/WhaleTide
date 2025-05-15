package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 确认订单VO
 */
@Data
@Schema(description = "确认订单信息")
public class ConfirmOrderVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 订单项列表
     */
    @Schema(description = "订单项列表")
    private List<OrderItemVO> orderItems;
    
    /**
     * 收货地址列表
     */
    @Schema(description = "收货地址列表")
    private List<AddressVO> addresses;
    
    /**
     * 订单总金额
     */
    @Schema(description = "订单总金额")
    private Double totalAmount;
} 