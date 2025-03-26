package com.whale_tide.common.exception.cart;

import com.whale_tide.common.exception.base.CartException;

/**
 * 购物车项目数量超限异常
 */
public class CartItemLimitExceededException extends CartException {
    
    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public CartItemLimitExceededException(String message) {
        super(message, SUB_CODE);
    }
} 