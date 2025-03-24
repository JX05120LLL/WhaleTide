package com.whale_tide.common.exception.cart;

import com.whale_tide.common.exception.base.CartException;

/**
 * 购物车项目未找到异常
 */
public class CartItemNotFoundException extends CartException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public CartItemNotFoundException(String message) {
        super(message, SUB_CODE);
    }
} 