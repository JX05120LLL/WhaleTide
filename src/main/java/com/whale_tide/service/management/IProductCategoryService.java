package com.whale_tide.service.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.management.product.ProductCategoryDto;
import com.whale_tide.dto.management.product.ProductCategoryParam;

import java.util.List;
import java.util.Map;

/**
 * 商品分类服务接口
 */
public interface IProductCategoryService {
    
    /**
     * 获取分页分类列表
     */
    Page<ProductCategoryDto> getList(Long parentId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取所有一级分类及其子分类
     */
    List<Map<String, Object>> getListWithChildren();
    
    /**
     * 获取单个分类详情
     */
    ProductCategoryDto getItem(Long id);
    
    /**
     * 创建商品分类
     */
    Long create(ProductCategoryParam productCategoryParam);
    
    /**
     * 更新商品分类
     */
    int update(Long id, ProductCategoryParam productCategoryParam);
    
    /**
     * 删除商品分类
     */
    int delete(Long id);

    /**
     * 更新导航状态
     */
    int updateNavStatus(Long id, Integer navStatus);

    /**
     * 批量更新导航状态
     */
    int updateNavStatus(List<Long> ids, Integer navStatus);

    /**
     * 更新显示状态
     */
    int updateShowStatus(Long id, Integer showStatus);
    
    /**
     * 批量更新显示状态
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);
} 