package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.CartException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 购物车异常处理器
 */
@RestControllerAdvice
public class CartExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对CartException的处理（继承自BusinessException）
} 