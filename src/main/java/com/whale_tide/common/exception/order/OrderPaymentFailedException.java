package com.whale_tide.common.exception.order;

import com.whale_tide.common.exception.base.OrderException;

/**
 * 订单支付失败异常
 */
public class OrderPaymentFailedException extends OrderException {
    
    // 子错误码：4
    private static final int SUB_CODE = 4;
    
    public OrderPaymentFailedException(String message) {
        super(message, SUB_CODE);
    }
} 