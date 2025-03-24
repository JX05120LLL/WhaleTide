package com.whale_tide.common.exception.address;

import com.whale_tide.common.exception.base.AddressException;

/**
 * 地址数量超限异常
 */
public class AddressLimitExceededException extends AddressException {
    
    // 子错误码：2
    private static final int SUB_CODE = 2;
    
    public AddressLimitExceededException(String message) {
        super(message, SUB_CODE);
    }
} 