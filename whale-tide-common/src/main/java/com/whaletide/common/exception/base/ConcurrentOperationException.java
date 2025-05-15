package com.whaletide.common.exception.base;

/**
 * 并发操作异常
 * 用于处理并发操作导致的冲突，如乐观锁异常等
 */
public class ConcurrentOperationException extends BusinessException {
    
    public ConcurrentOperationException(String message) {
        super(message);
    }
    
    public ConcurrentOperationException(String resourceType, Object resourceId) {
        super("资源[" + resourceType + ":" + resourceId + "]被并发修改，请稍后重试");
    }
    
    public ConcurrentOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 