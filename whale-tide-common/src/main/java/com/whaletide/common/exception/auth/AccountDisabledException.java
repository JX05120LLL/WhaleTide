package com.whaletide.common.exception.auth;

/**
 * 账户禁用异常
 * 用于处理用户账户被禁用的情况
 */
public class AccountDisabledException extends AuthenticationException {

    public AccountDisabledException() {
        super("账户已被禁用，请联系管理员");
    }
    
    public AccountDisabledException(String username) {
        super("账户[" + username + "]已被禁用，请联系管理员");
    }
    
    public AccountDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
} 