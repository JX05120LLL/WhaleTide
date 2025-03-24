package com.whale_tide.common.exception.base;

/**
 * 用户地址相关异常基类
 */
public abstract class AddressException extends BusinessException {
    
    // 地址模块错误码前缀：5000
    private static final int MODULE_CODE_PREFIX = 5000;
    
    public AddressException(String message, int subCode) {
        super(message, MODULE_CODE_PREFIX + subCode);
    }
} 