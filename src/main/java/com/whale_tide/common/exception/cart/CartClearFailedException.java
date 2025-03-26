package com.whale_tide.common.exception.cart;

import com.whale_tide.common.exception.base.CartException;

/**
 * 购物车清空失败异常
 */
public class CartClearFailedException extends CartException {
    
    // 子错误码：4
    private static final int SUB_CODE = 4;
    
    public CartClearFailedException(String message) {
        super(message, SUB_CODE);
    }
} 