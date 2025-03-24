package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * 产品价格无效异常
 */
public class ProductPriceInvalidException extends ProductException {
    
    // 子错误码：4
    private static final int SUB_CODE = 4;
    
    public ProductPriceInvalidException(String message) {
        super(message, SUB_CODE);
    }
} 