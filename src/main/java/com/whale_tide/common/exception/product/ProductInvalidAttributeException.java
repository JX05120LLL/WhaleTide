package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * 商品属性无效异常
 */
public class ProductInvalidAttributeException extends ProductException {
    
    private static final int SUB_CODE = 5;
    
    public ProductInvalidAttributeException(String message) {
        super(message, SUB_CODE);
    }
} 