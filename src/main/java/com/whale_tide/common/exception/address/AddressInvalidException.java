package com.whale_tide.common.exception.address;

import com.whale_tide.common.exception.base.AddressException;

/**
 * 地址信息无效异常
 */
public class AddressInvalidException extends AddressException {
    
    // 子错误码：3
    private static final int SUB_CODE = 3;
    
    public AddressInvalidException(String message) {
        super(message, SUB_CODE);
    }
} 