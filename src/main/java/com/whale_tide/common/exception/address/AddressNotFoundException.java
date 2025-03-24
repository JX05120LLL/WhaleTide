package com.whale_tide.common.exception.address;

import com.whale_tide.common.exception.base.AddressException;

/**
 * 地址未找到异常
 */
public class AddressNotFoundException extends AddressException {
    
    // 子错误码：1
    private static final int SUB_CODE = 1;
    
    public AddressNotFoundException(String message) {
        super(message, SUB_CODE);
    }
} 