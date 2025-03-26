package com.whale_tide.common.exception.cart;

import com.whale_tide.common.exception.base.CartException;

/**
 * 商品库存不足异常
 */
public class ProductOutOfStockException extends CartException {
    
    // 子错误码：5
    private static final int SUB_CODE = 5;
    
    public ProductOutOfStockException(String message) {
        super(message, SUB_CODE);
    }
} 