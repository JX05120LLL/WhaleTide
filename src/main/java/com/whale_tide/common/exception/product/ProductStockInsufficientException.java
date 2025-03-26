package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * 产品库存不足异常
 */
public class ProductStockInsufficientException extends ProductException {
    
    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public ProductStockInsufficientException(String message) {
        super(message, SUB_CODE);
    }
} 