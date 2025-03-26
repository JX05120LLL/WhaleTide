package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * 产品已存在异常
 */
public class ProductAlreadyExistsException extends ProductException {
    
    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public ProductAlreadyExistsException(String message) {
        super(message, SUB_CODE);
    }
} 