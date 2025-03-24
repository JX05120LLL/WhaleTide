package com.whale_tide.common.exception.brand;

import com.whale_tide.common.exception.base.BrandException;

/**
 * 品牌产品关联异常
 */
public class BrandProductRelationException extends BrandException {
    
    // 子错误码：5
    private static final int SUB_CODE = 5;
    
    public BrandProductRelationException(String message) {
        super(message, SUB_CODE);
    }
} 