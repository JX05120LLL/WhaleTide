package com.whaletide.common.exception.service;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 远程服务异常
 * 用于处理调用外部服务（如第三方API）失败的情况
 */
public class RemoteServiceException extends BusinessException {
    
    public RemoteServiceException(String message) {
        super(message);
    }
    
    public RemoteServiceException(String serviceName, String errorMessage) {
        super("调用远程服务[" + serviceName + "]失败: " + errorMessage);
    }
    
    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
} 