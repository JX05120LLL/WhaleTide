package com.whale_tide.common.exception.auth;

import com.whale_tide.common.exception.base.AuthException;

/**
 * 未授权异常
 */
public class UnauthorizedException extends AuthException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public UnauthorizedException(String message) {
        super(message, SUB_CODE);
    }
} 