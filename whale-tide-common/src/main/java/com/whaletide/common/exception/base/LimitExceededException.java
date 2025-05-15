package com.whaletide.common.exception.base;

/**
 * 限制超出异常
 * 用于处理各种限制超出的情况，如频率限制、数量限制等
 */
public class LimitExceededException extends BusinessException {
    
    public LimitExceededException(String message) {
        super(message);
    }
    
    public LimitExceededException(String limitType, int maxLimit) {
        super(limitType + "超出最大限制：" + maxLimit);
    }
    
    public LimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
} 