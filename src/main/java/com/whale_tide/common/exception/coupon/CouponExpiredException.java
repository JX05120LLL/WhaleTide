package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券已过期异常
 */
public class CouponExpiredException extends CouponException {
    
    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public CouponExpiredException(String message) {
        super(message, SUB_CODE);
    }
} 