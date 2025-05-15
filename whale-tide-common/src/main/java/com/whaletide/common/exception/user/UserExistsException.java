package com.whaletide.common.exception.user;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 用户已存在异常
 */
public class UserExistsException extends BusinessException {

    public UserExistsException(String username) {
        super("用户名[" + username + "]已被注册");
    }
    
    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 