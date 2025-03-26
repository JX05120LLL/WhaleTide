package com.whale_tide.common.exception.user;

import com.whale_tide.common.exception.base.UserException;

/**
 * 用户名已经存在异常
 */
public class UsernameHasExistedException extends UserException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public UsernameHasExistedException(String message) {
        super(message, SUB_CODE);
    }
}
