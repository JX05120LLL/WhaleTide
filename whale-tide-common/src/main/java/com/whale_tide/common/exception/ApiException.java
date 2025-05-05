package com.whale_tide.common.exception;


public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
