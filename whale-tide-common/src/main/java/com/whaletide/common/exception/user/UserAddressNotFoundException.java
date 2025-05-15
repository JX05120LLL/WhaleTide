package com.whaletide.common.exception.user;

import com.whaletide.common.exception.base.ResourceNotFoundException;

/**
 * 用户地址不存在异常
 */
public class UserAddressNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 1L;

    public UserAddressNotFoundException(String message) {
        super(message);
    }

    public UserAddressNotFoundException(Long addressId) {
        super("用户地址不存在: " + addressId);
    }

    public UserAddressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 