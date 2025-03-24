package com.whale_tide.common.exception.base;

/**
 * 业务异常基类
 */
public abstract class BusinessException extends RuntimeException {
    
    private final int code;
    
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }


    public int getCode() {
        return code;
    }
} 