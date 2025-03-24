package com.whale_tide.common.exception.order;

import com.whale_tide.common.exception.base.OrderException;

/**
 * 订单创建失败异常
 */
public class OrderCreateFailedException extends OrderException {
    
    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public OrderCreateFailedException(String message) {
        super(message, SUB_CODE);
    }
} 