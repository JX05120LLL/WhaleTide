package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.CouponException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 优惠券异常处理器
 */
@RestControllerAdvice
public class CouponExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对CouponException的处理（继承自BusinessException）
} 