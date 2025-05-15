package com.whaletide.common.exception.base;

/**
 * 资源未找到异常
 * 用于处理请求的资源不存在的情况
 */
public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(resourceType + "[" + identifier + "]不存在");
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 