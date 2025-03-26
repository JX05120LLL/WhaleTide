package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * 产品未找到异常
 */
public class ProductNotFoundException extends ProductException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public ProductNotFoundException(String message) {
        super(message, SUB_CODE);
    }
} 