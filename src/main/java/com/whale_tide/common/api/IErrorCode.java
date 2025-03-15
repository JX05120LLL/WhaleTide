package com.whale_tide.common.api;

/**
 * 封装API的错误码
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    long getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
