package com.whale_tide.common.exception.base;

/**
 * 用户相关异常基类
 */
public abstract class UserException extends BusinessException {
    
    // 用户模块错误码前缀：1000
    private static final int MODULE_CODE_PREFIX = 1000;
    
    public UserException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 