package com.whale_tide.common.exception.user;

import com.whale_tide.common.exception.base.UserException;

/**
 * 用户凭证错误异常
 */
public class BadCredentialsException extends UserException {

    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public BadCredentialsException(String message) {
        super(message, SUB_CODE);
    }
}
