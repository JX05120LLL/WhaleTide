package com.whaletide.common.exception;

/**
 * 未授权异常
 */
public class UnauthorizedException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
} 