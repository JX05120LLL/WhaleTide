package com.whaletide.common.exception.auth;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 用户认证异常
 * 用于处理用户认证失败的情况，避免与Spring Security的AuthenticationException冲突
 */
public class UserAuthenticationException extends BusinessException {

    public UserAuthenticationException(String username) {
        super("用户[" + username + "]认证失败");
    }
    
    public UserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 