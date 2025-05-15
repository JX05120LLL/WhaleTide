package com.whaletide.common.exception.base;

/**
 * 业务异常基类
 * 所有业务异常都应继承此类
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 