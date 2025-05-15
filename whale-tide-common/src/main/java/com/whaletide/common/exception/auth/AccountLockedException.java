package com.whaletide.common.exception.auth;

/**
 * 账户锁定异常
 * 用于处理用户账户被锁定的情况
 */
public class AccountLockedException extends AuthenticationException {

    public AccountLockedException() {
        super("账户已被锁定，请联系管理员");
    }
    
    public AccountLockedException(String username) {
        super("账户[" + username + "]已被锁定，请联系管理员");
    }
    
    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
    }
} 