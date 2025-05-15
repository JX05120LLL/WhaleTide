package com.whaletide.common.exception.auth;

/**
 * 验证码错误异常
 * 用于处理验证码不正确、过期或缺失的情况
 */
public class VerificationCodeException extends AuthenticationException {

    public VerificationCodeException() {
        super("验证码错误或已过期");
    }
    
    public VerificationCodeException(String message) {
        super(message);
    }
    
    public VerificationCodeException(String message, Throwable cause) {
        super(message, cause);
    }
} 