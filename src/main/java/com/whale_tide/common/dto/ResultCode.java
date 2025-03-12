package com.whale_tide.common.dto;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "success"),
    FAILURE(500, "failed");

    final int code;
    final String message;


    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
