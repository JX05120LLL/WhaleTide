package com.whale_tide.common.exception.auth;

import com.whale_tide.common.exception.base.AuthException;

/**
 * Token过期异常
 */
public class TokenExpiredException extends AuthException {
    
    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public TokenExpiredException(String message) {
        super(message, SUB_CODE);
    }
} 