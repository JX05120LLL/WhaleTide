package com.whale_tide.common.api;


import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class PageRequest {
    public Long pageNum;  // 当前页码，从1开始
    public Long pageSize; // 每页记录数
}
