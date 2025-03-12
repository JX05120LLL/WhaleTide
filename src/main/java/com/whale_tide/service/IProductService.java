package com.whale_tide.service;

import com.whale_tide.entity.PmsProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品服务接口
 */
public interface IProductService {
    
    /**
     * 创建商品
     * @param product 商品信息
     * @return 创建成功返回商品信息
     */
    PmsProducts create(PmsProducts product);
    
    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    PmsProducts getById(Long id);
    
    /**
     * 更新商品信息
     * @param id 商品ID
     * @param product 商品信息
     * @return 更新成功返回true
     */
    boolean update(Long id, PmsProducts product);
    
    /**
     * 删除商品
     * @param id 商品ID
     * @return 删除成功返回true
     */
    boolean delete(Long id);
    
    /**
     * 批量删除商品
     * @param ids 商品ID列表
     * @return 删除成功返回true
     */
    boolean deleteBatch(List<Long> ids);
    
    /**
     * 分页查询商品
     * @param keyword 关键字
     * @param brandId 品牌ID
     * @param categoryId 分类ID
     * @param publishStatus 上架状态
     * @param pageable 分页参数
     * @return 商品分页列表
     */
    Page<PmsProducts> list(String keyword, Long brandId, Long categoryId, Integer publishStatus, Pageable pageable);
    
    /**
     * 更新商品上架状态
     * @param ids 商品ID列表
     * @param publishStatus 上架状态（0->下架；1->上架）
     * @return 更新成功返回true
     */
    boolean updatePublishStatus(List<Long> ids, Integer publishStatus);
    
    /**
     * 更新商品推荐状态
     * @param ids 商品ID列表
     * @param recommendStatus 推荐状态（0->不推荐；1->推荐）
     * @return 更新成功返回true
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);
    
    /**
     * 更新商品新品状态
     * @param ids 商品ID列表
     * @param newStatus 新品状态（0->不是新品；1->新品）
     * @return 更新成功返回true
     */
    boolean updateNewStatus(List<Long> ids, Integer newStatus);
    
    /**
     * 更新商品删除状态
     * @param ids 商品ID列表
     * @param deleteStatus 删除状态（0->未删除；1->已删除）
     * @return 更新成功返回true
     */
    boolean updateDeleteStatus(List<Long> ids, Integer deleteStatus);
} 