package com.whale_tide.service;

import com.whale_tide.dto.resource.ResourceCategoryParam;
import com.whale_tide.dto.resource.ResourceCategoryResult;

import java.util.List;

/**
 * 资源分类管理Service接口
 */
public interface IResourceCategoryService {
    /**
     * 获取所有资源分类
     */
    List<ResourceCategoryResult> listAll();
    
    /**
     * 创建资源分类
     */
    boolean create(ResourceCategoryParam resourceCategoryParam);
    
    /**
     * 更新资源分类
     */
    boolean update(Long id, ResourceCategoryParam resourceCategoryParam);
    
    /**
     * 删除资源分类
     */
    boolean delete(Long id);
} 