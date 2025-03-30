package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券未找到异常
 */
public class CouponNotFoundException extends CouponException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public CouponNotFoundException(String message) {
        super(message, SUB_CODE);
    }
} 