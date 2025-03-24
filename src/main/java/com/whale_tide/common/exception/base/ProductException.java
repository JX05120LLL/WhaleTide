package com.whale_tide.common.exception.base;

/**
 * 产品相关异常基类
 */
public abstract class ProductException extends BusinessException {
    
    // 产品模块错误码前缀：2000
    private static final int MODULE_CODE_PREFIX = 2000;
    
    public ProductException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 