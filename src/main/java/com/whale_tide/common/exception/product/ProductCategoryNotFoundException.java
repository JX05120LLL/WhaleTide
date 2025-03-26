package com.whale_tide.common.exception.product;

import com.whale_tide.common.exception.base.ProductException;

/**
 * 产品分类未找到异常
 */
public class ProductCategoryNotFoundException extends ProductException {
    
    // 子错误码：5
    private static final int SUB_CODE = 5;
    
    public ProductCategoryNotFoundException(String message) {
        super(message, SUB_CODE);
    }
} 