package com.whale_tide.common.exception.base;

/**
 * 品牌相关异常基类
 */
public abstract class BrandException extends BusinessException {
    
    // 品牌模块错误码前缀：4000
    private static final int MODULE_CODE_PREFIX = 4000;
    
    public BrandException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 