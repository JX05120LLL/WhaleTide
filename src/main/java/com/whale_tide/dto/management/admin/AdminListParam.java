package com.whale_tide.dto.management.admin;

import lombok.Data;

@Data
public class AdminListParam {
    /**
     * 搜索关键词
     */
    private String keyword;
    /**
     * 页码
     */
    private long pageNum;
    /**
     * 每页记录数
     */
    private long pageSize;
}
