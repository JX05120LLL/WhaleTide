package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.AddressException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 地址异常处理器
 */
@RestControllerAdvice
public class AddressExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对AddressException的处理（继承自BusinessException）
} 