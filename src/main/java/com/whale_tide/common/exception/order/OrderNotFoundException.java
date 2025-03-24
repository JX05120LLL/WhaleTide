package com.whale_tide.common.exception.order;

import com.whale_tide.common.exception.base.OrderException;

/**
 * 订单未找到异常
 */
public class OrderNotFoundException extends OrderException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public OrderNotFoundException(String message) {
        super(message, SUB_CODE);
    }
} 