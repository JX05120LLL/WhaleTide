package com.whale_tide.common.exception.brand;

import com.whale_tide.common.exception.base.BrandException;

/**
 * 品牌Logo无效异常
 */
public class BrandLogoInvalidException extends BrandException {
    
    // 子错误码：6
    private static final int SUB_CODE = 6;
    
    public BrandLogoInvalidException(String message) {
        super(message, SUB_CODE);
    }
} 