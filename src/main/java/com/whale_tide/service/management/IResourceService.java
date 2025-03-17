package com.whale_tide.service.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.dto.management.resource.ResourceParam;
import com.whale_tide.dto.management.resource.ResourceResult;

import java.util.List;

/**
 * 资源管理Service接口
 */
public interface IResourceService {
    /**
     * 获取资源列表（分页）
     */
    IPage<ResourceResult> list(String nameKeyword, String urlKeyword, Long categoryId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取所有资源列表
     */
    List<ResourceResult> listAll();
    
    /**
     * 创建资源
     */
    boolean create(ResourceParam resourceParam);
    
    /**
     * 更新资源
     */
    boolean update(Long id, ResourceParam resourceParam);
    
    /**
     * 删除资源
     */
    boolean delete(Long id);
    
    /**
     * 获取单个资源详情
     */
    ResourceResult getItem(Long id);
} 