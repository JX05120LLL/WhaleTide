package com.whale_tide.common.exception.base;

/**
 * 购物车相关异常基类
 */
public abstract class CartException extends BusinessException {
    
    // 购物车模块错误码前缀：6000
    private static final int MODULE_CODE_PREFIX = 6000;
    
    public CartException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 