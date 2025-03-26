package com.whale_tide.common.exception.brand;

import com.whale_tide.common.exception.base.BrandException;

/**
 * 品牌更新失败异常
 */
public class BrandUpdateFailedException extends BrandException {
    
    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public BrandUpdateFailedException(String message) {
        super(message, SUB_CODE);
    }
} 