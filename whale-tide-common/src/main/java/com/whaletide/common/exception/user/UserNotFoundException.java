package com.whaletide.common.exception.user;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 用户不存在异常
 */
public class UserNotFoundException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public UserNotFoundException() {
        super("用户不存在");
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 