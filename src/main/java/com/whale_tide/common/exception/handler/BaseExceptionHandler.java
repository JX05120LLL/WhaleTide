package com.whale_tide.common.exception.handler;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.exception.base.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 基础异常处理器
 */
public abstract class BaseExceptionHandler {
    
    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 通用结果
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResult<String> handleBusinessException(BusinessException e) {
        return CommonResult.failed(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理运行时异常
     * @param e 运行时异常
     * @return 通用结果
     */
    @ExceptionHandler(RuntimeException.class)
    public CommonResult<String> handleRuntimeException(RuntimeException e) {
        return CommonResult.failed("系统异常：" + e.getMessage());
    }
} 