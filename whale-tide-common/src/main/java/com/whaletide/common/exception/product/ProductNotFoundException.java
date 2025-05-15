package com.whaletide.common.exception.product;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 商品不存在异常
 */
public class ProductNotFoundException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public ProductNotFoundException() {
        super("商品不存在");
    }
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 