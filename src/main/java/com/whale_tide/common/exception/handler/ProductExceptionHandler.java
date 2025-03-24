package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.ProductException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 产品异常处理器
 */
@RestControllerAdvice
public class ProductExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对ProductException的处理（继承自BusinessException）
} 