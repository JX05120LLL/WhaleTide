package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.OrderException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 订单异常处理器
 */
@RestControllerAdvice
public class OrderExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对OrderException的处理（继承自BusinessException）
} 