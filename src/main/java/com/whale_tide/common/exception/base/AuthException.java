package com.whale_tide.common.exception.base;

/**
 * 认证相关异常基类
 */
public abstract class AuthException extends BusinessException {
    
    // 认证模块错误码前缀：7000
    private static final int MODULE_CODE_PREFIX = 7000;
    
    public AuthException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 