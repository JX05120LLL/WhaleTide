package com.whale_tide.common.exception.address;

import com.whale_tide.common.exception.base.AddressException;

/**
 * 设置默认地址失败异常
 */
public class DefaultAddressSetFailedException extends AddressException {
    
    // 子错误码：4
    private static final int SUB_CODE = 4;
    
    public DefaultAddressSetFailedException(String message) {
        super(message, SUB_CODE);
    }
} 