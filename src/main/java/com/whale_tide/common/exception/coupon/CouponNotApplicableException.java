package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券不适用异常（例如：商品类型不匹配）
 */
public class CouponNotApplicableException extends CouponException {
    
    // 子错误码：6
    private static final int SUB_CODE = 6;
    
    public CouponNotApplicableException(String message) {
        super(message, SUB_CODE);
    }
} 