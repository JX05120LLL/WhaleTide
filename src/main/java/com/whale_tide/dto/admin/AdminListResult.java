package com.whale_tide.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminListResult {
    /**
     * 管理员列表
     */
    private List<AdminListDTO> list;
    /**
     * 查询结果总计数
     */
    private long total;
    /**
     * 页码
     */
    private long pageNum;
    /**
     * 每页大小
     */
    private long pageSize;
}
