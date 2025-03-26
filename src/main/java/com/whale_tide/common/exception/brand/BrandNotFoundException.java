package com.whale_tide.common.exception.brand;

import com.whale_tide.common.exception.base.BrandException;

/**
 * 品牌未找到异常
 */
public class BrandNotFoundException extends BrandException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public BrandNotFoundException(String message) {
        super(message, SUB_CODE);
    }
} 