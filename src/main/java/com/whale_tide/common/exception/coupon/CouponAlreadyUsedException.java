package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券已经被使用异常
 */
public class CouponAlreadyUsedException extends CouponException {
    
    
    // 子错误码：5
    private static final int SUB_CODE = 5;
    
    public CouponAlreadyUsedException(String message) {
        super(message, SUB_CODE);
    }
} 