package com.whaletide.common.exception.product;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 商品库存异常
 */
public class ProductStockException extends BusinessException {
    
    public ProductStockException(String message) {
        super(message);
    }
    
    public ProductStockException(String productId, int requestQuantity, int availableStock) {
        super("商品[" + productId + "]库存不足，当前库存:" + availableStock + "，请求数量:" + requestQuantity);
    }
    
    public ProductStockException(String message, Throwable cause) {
        super(message, cause);
    }
} 