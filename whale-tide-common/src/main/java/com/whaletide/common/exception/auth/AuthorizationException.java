package com.whaletide.common.exception.auth;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 授权异常
 * 用于处理用户权限不足的情况
 */
public class AuthorizationException extends BusinessException {
    
    public AuthorizationException(String message) {
        super(message);
    }
    
    public AuthorizationException(String resource, String operation) {
        super("没有权限" + operation + "资源[" + resource + "]");
    }
    
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
} 