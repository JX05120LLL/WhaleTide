package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券领取次数超限异常
 */
public class CouponReceiveLimitExceededException extends CouponException {
    
    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public CouponReceiveLimitExceededException(String message) {
        super(message, SUB_CODE);
    }
} 