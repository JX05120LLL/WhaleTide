package com.whale_tide.common.exception.order;

import com.whale_tide.common.exception.base.OrderException;

/**
 * 订单取消失败异常
 */
public class OrderCancelFailedException extends OrderException {
    
    // 子错误码：5
    private static final int SUB_CODE = 5;
    
    public OrderCancelFailedException(String message) {
        super(message, SUB_CODE);
    }
} 