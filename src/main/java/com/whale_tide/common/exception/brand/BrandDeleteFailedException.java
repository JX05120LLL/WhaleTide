package com.whale_tide.common.exception.brand;

import com.whale_tide.common.exception.base.BrandException;

/**
 * 品牌删除失败异常
 */
public class BrandDeleteFailedException extends BrandException {
    
    // 子错误码：4
    private static final int SUB_CODE = 4;
    
    public BrandDeleteFailedException(String message) {
        super(message, SUB_CODE);
    }
} 