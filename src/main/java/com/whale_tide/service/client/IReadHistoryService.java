package com.whale_tide.service.client;

import com.whale_tide.common.api.PageRequest;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.readHistory.ReadHistoryCreateRequest;
import com.whale_tide.dto.client.readHistory.ReadHistoryDeleteRequest;
import com.whale_tide.dto.client.readHistory.ReadHistoryResponse;

/**
 * 搜索历史服务接口
 */
public interface IReadHistoryService {
    
    /**
     * 获取搜索历史记录
     * @return 分页搜索历史记录
     */
    PageResponse<ReadHistoryResponse> getList(PageRequest pageRequest);
    
    /**
     * 添加搜索历史记录
     * @param request 搜索历史记录参数
     */
    void create(ReadHistoryCreateRequest request);
    
    /**
     * 删除搜索历史记录
     * @param request 删除请求参数
     */
    void delete(ReadHistoryDeleteRequest request);
    
    /**
     * 清空搜索历史记录
     */
    void clear();
} 