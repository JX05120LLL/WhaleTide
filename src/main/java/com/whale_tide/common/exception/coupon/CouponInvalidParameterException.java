package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券参数无效异常
 */
public class CouponInvalidParameterException extends CouponException {
    
    // 子错误码：8
    private static final int SUB_CODE = 8;
    
    public CouponInvalidParameterException(String message) {
        super(message, SUB_CODE);
    }
} 