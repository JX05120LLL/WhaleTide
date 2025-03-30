package com.whale_tide.common.exception.base;

/**
 * 优惠券相关异常基类
 */
public abstract class CouponException extends BusinessException {
    
    // 优惠券模块错误码前缀：8000
    private static final int MODULE_CODE_PREFIX = 8000;
    
    public CouponException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 