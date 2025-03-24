package com.whale_tide.common.exception.base;

/**
 * 订单相关异常基类
 */
public abstract class OrderException extends BusinessException {
    
    // 订单模块错误码前缀：3000
    private static final int MODULE_CODE_PREFIX = 3000;
    
    public OrderException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 