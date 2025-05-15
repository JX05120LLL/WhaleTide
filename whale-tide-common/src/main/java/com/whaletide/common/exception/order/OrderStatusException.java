package com.whaletide.common.exception.order;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 订单状态异常
 */
public class OrderStatusException extends BusinessException {
    
    public OrderStatusException(String message) {
        super(message);
    }
    
    public OrderStatusException(String orderId, String currentStatus, String targetStatus) {
        super("订单[" + orderId + "]当前状态为[" + currentStatus + "]，不能变更为[" + targetStatus + "]");
    }
    
    public OrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }
} 