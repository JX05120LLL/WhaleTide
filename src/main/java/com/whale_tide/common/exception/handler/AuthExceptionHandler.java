package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.AuthException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 认证异常处理器
 */
@RestControllerAdvice
public class AuthExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对AuthException的处理（继承自BusinessException）
} 