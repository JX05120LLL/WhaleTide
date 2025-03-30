package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券已领完异常（库存不足）
 */
public class CouponOutOfStockException extends CouponException {
    
    // 子错误码：7
    private static final int SUB_CODE = 7;
    
    public CouponOutOfStockException(String message) {
        super(message, SUB_CODE);
    }
} 