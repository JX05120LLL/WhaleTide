package com.whaletide.common.exception.auth;

/**
 * 令牌无效异常
 * 用于处理JWT令牌无效、过期或被篡改的情况
 */
public class TokenInvalidException extends AuthenticationException {

    public TokenInvalidException() {
        super("令牌无效或已过期");
    }
    
    public TokenInvalidException(String message) {
        super(message);
    }
    
    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
} 