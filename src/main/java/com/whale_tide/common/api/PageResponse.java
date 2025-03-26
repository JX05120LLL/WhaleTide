package com.whale_tide.common.api;

import lombok.Data;

import java.util.List;

/**
 * 分页响应对象
 */
@Data
public class PageResponse<T> {
    private List<T> list;     // 当前页数据列表
    private int pageNum;      // 当前页码
    private int pageSize;     // 每页记录数
    private long total;       // 总记录数
    private int totalPage;    // 总页数
    
    /**
     * 无参构造函数
     */
    public PageResponse() {
    }

    /**
     * 构造方法
     */
    public PageResponse(List<T> list, int pageNum, int pageSize, long total, int totalPage) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPage = totalPage;
    }

    /**
     * 创建分页响应对象
     */
    public static <T> PageResponse<T> of(List<T> list, int pageNum, int pageSize, long total, int totalPage) {
        return new PageResponse<>(list, pageNum, pageSize, total, totalPage);
    }
}
