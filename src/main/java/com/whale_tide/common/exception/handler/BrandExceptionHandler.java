package com.whale_tide.common.exception.handler;

import com.whale_tide.common.exception.base.BrandException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 品牌异常处理器
 */
@RestControllerAdvice
public class BrandExceptionHandler extends BaseExceptionHandler {
    // 基础异常处理器已经包含了对BrandException的处理（继承自BusinessException）
} 