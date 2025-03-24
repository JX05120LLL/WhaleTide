package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.UserException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 用户异常处理器
 */
@RestControllerAdvice
public class UserExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对UserException的处理（继承自BusinessException）
} 