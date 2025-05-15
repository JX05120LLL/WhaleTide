package com.whaletide.common.exception.config;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 配置异常
 * 用于处理系统配置错误或缺失的情况
 */
public class ConfigurationException extends BusinessException {
    
    public ConfigurationException(String message) {
        super(message);
    }
    
    public ConfigurationException(String configName, String reason) {
        super("配置[" + configName + "]错误: " + reason);
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
} 