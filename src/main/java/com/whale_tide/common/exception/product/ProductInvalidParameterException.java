package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * 商品参数无效异常
 */
public class ProductInvalidParameterException extends ProductException {
    
    private static final int SUB_CODE = 1;
    
    public ProductInvalidParameterException(String message) {
        super(message, SUB_CODE);
    }
} 