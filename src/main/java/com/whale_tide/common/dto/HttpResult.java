package com.whale_tide.common.dto;


import lombok.Data;

import java.util.Date;

/**
 * 返回前端的数据集
 * @param <T>
 */


@Data
public class HttpResult <T> {
    private int code;
    private String message;
    private T result;
    private Date time;


    public static <T> HttpResult<T> success(T result){
        HttpResult<T> httpResult = new HttpResult<T>();
        httpResult.setCode(ResultCode.SUCCESS.getCode());
        httpResult.setMessage(ResultCode.SUCCESS.getMessage());
        httpResult.setResult(result);
        httpResult.setTime(new Date());
        return httpResult;
    }

    public static <T> HttpResult<T> failed(T result){
        HttpResult<T> httpResult = new HttpResult<T>();
        httpResult.setCode(ResultCode.FAILURE.getCode());
        httpResult.setMessage(ResultCode.FAILURE.getMessage());
        httpResult.setResult(result);
        httpResult.setTime(new Date());
        return httpResult;
    }

    // 重载构造方法，不传参
    public static <T> HttpResult<T> failed(){
       return failed(null);
    }

}
