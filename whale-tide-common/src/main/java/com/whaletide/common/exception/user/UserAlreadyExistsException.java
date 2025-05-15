package com.whaletide.common.exception.user;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 用户已存在异常
 */
public class UserAlreadyExistsException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public UserAlreadyExistsException() {
        super("用户已存在");
    }
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 