package com.whale_tide.common.exception.cart;

import com.whale_tide.common.exception.base.CartException;

/**
 * 购物车更新失败异常
 */
public class CartUpdateFailedException extends CartException {
    
    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public CartUpdateFailedException(String message) {
        super(message, SUB_CODE);
    }
} 