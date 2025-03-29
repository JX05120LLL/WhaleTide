package com.whale_tide.common.exception.auth;

import com.whale_tide.common.exception.base.AuthException;

/**
 * 优惠券不属于用户异常
 */
public class CouponNotBelongToUserException extends AuthException {
    
    // 子错误码：4
    private static final int SUB_CODE = 4;
    
    public CouponNotBelongToUserException(String message) {
        super(message, SUB_CODE);
    }
} 