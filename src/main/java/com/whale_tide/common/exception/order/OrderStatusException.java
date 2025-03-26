package com.whale_tide.common.exception.order;

import com.whale_tide.common.exception.base.OrderException;

/**
 * 订单状态异常
 */
public class OrderStatusException extends OrderException {
    
    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public OrderStatusException(String message) {
        super(message, SUB_CODE);
    }
} 