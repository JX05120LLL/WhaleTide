package com.whale_tide.common.exception.user;

import com.whale_tide.common.exception.base.UserException;

/**
 * 用户名不存在异常
 */
public class UsernameNotExistException extends UserException {

    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public UsernameNotExistException(String message) {
        super(message, SUB_CODE);
    }
}
