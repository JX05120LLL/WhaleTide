package com.whaletide.common.exception.order;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 订单不存在异常
 */
public class OrderNotFoundException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public OrderNotFoundException() {
        super("订单不存在");
    }
    
    public OrderNotFoundException(String message) {
        super(message);
    }
    
    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 