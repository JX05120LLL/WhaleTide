package com.whale_tide.common.exception.address;

import com.whale_tide.common.exception.base.AddressException;

/**
 * 地址删除失败异常
 */
public class AddressDeleteFailedException extends AddressException {
    
    // 子错误码：5
    private static final int SUB_CODE = 5;
    
    public AddressDeleteFailedException(String message) {
        super(message, SUB_CODE);
    }
} 