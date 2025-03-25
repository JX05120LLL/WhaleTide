package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * SKU编码重复异常
 */
public class ProductDuplicateSkuException extends ProductException {
    
    private static final int SUB_CODE = 2;
    
    public ProductDuplicateSkuException(String message) {
        super(message, SUB_CODE);
    }
} 