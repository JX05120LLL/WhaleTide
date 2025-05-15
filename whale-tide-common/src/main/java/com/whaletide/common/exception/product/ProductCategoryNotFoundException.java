package com.whaletide.common.exception.product;

import com.whaletide.common.exception.base.ResourceNotFoundException;

/**
 * 商品分类不存在异常
 */
public class ProductCategoryNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 1L;

    public ProductCategoryNotFoundException(String message) {
        super(message);
    }

    public ProductCategoryNotFoundException(Long categoryId) {
        super("商品分类不存在: " + categoryId);
    }

    public ProductCategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 