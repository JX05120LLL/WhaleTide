package com.whaletide.common.exception.base;

/**
 * 参数验证异常
 * 用于处理参数校验失败的情况
 */
public class ValidationException extends BusinessException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String field, String message) {
        super("字段[" + field + "]验证错误: " + message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
} 