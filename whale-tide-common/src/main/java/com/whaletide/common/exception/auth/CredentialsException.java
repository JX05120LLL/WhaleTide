package com.whaletide.common.exception.auth;

/**
 * 用户名或密码错误异常
 * 用于处理用户登录时提供的凭证不正确的情况
 */
public class CredentialsException extends AuthenticationException {

    public CredentialsException() {
        super("用户名或密码错误");
    }
    
    public CredentialsException(String message) {
        super(message);
    }
    
    public CredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
} 