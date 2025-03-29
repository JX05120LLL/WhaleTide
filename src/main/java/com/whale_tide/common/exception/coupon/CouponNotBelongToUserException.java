package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券不属于当前用户异常
 */
public class CouponNotBelongToUserException extends CouponException {
    
    // 子错误码：9
    private static final int SUB_CODE = 9;
    
    public CouponNotBelongToUserException(String message) {
        super(message, SUB_CODE);
    }
} 