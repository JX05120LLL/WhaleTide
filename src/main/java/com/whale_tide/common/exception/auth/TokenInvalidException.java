package com.whale_tide.common.exception.auth;

import com.whale_tide.common.exception.base.AuthException;

/**
 * Token无效异常
 */
public class TokenInvalidException extends AuthException {
    
    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public TokenInvalidException(String message) {
        super(message, SUB_CODE);
    }
} 