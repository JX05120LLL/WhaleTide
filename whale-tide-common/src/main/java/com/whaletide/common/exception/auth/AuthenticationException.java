package com.whaletide.common.exception.auth;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 认证异常基类
 * 用于处理各种认证过程中的异常
 */
public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 