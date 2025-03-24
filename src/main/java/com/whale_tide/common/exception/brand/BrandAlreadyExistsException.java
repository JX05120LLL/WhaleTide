package com.whale_tide.common.exception.brand;

import com.whale_tide.common.exception.base.BrandException;

/**
 * 品牌已存在异常
 */
public class BrandAlreadyExistsException extends BrandException {
    
    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public BrandAlreadyExistsException(String message) {
        super(message, SUB_CODE);
    }
} 