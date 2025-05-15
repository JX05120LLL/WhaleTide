package com.whaletide.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatusEnum {
    /**
     * 待付款
     */
    PENDING_PAYMENT(0, "待付款"),
    
    /**
     * 待发货
     */
    PENDING_SHIPMENT(1, "待发货"),
    
    /**
     * 待收货
     */
    PENDING_RECEIPT(2, "待收货"),
    
    /**
     * 已完成
     */
    COMPLETED(3, "已完成"),
    
    /**
     * 已取消
     */
    CANCELED(4, "已取消"),
    
    /**
     * 已退款
     */
    REFUNDED(5, "已退款");
    
    @EnumValue
    @JsonValue
    private final Integer value;
    
    private final String desc;
    
    OrderStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    /**
     * 根据值获取枚举
     */
    public static OrderStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 判断是否可以取消
     */
    public boolean canCancel() {
        return this == PENDING_PAYMENT || this == PENDING_SHIPMENT;
    }
    
    /**
     * 判断是否可以支付
     */
    public boolean canPay() {
        return this == PENDING_PAYMENT;
    }
    
    /**
     * 判断是否可以发货
     */
    public boolean canShip() {
        return this == PENDING_SHIPMENT;
    }
    
    /**
     * 判断是否可以收货
     */
    public boolean canReceive() {
        return this == PENDING_RECEIPT;
    }
    
    /**
     * 判断是否可以申请退款
     */
    public boolean canRefund() {
        return this == PENDING_SHIPMENT || this == PENDING_RECEIPT;
    }
} 