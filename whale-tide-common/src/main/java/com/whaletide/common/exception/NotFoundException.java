package com.whaletide.common.exception;

/**
 * 资源未找到异常
 */
public class NotFoundException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 