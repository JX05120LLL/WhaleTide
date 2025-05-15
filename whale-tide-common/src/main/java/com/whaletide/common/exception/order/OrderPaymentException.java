package com.whaletide.common.exception.order;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 订单支付异常类
 */
public class OrderPaymentException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public OrderPaymentException(String message) {
        super(message);
    }

    public OrderPaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderPaymentException(Throwable cause) {
        super("订单支付异常", cause);
    }
} 