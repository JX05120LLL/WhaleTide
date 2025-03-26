package com.whale_tide.common.exception.brand;

import com.whale_tide.common.exception.base.BrandException;

/**
 * 品牌状态更新异常
 */
public class BrandStatusUpdateException extends BrandException {
    
    // 子错误码：7
    private static final int SUB_CODE = 7;
    
    public BrandStatusUpdateException(String message) {
        super(message, SUB_CODE);
    }
} 