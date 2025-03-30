package com.whale_tide.common.exception.coupon;

import com.whale_tide.common.exception.base.CouponException;

/**
 * 优惠券使用条件不满足异常（例如：订单金额未达到使用门槛）
 */
public class CouponUseConditionNotMetException extends CouponException {
    
    // 子错误码：4
    private static final int SUB_CODE = 4;
    
    public CouponUseConditionNotMetException(String message) {
        super(message, SUB_CODE);
    }
} 