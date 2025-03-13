package com.whale_tide.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale_tide.entity.PmsProducts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品数据访问接口
 */
@Mapper
public interface PmsProductsMapper extends BaseMapper<PmsProducts> {

    /**
     * 插入商品
     * @param product 商品信息
     * @return 影响行数
     */
    int insert(PmsProducts product);
    
    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品信息
     */
    PmsProducts selectById(Long id);
    
    /**
     * 更新商品
     * @param product 商品信息
     * @return 影响行数
     */
    int updateById(PmsProducts product);
    
    /**
     * 删除商品
     * @param id 商品ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    /**
     * 批量删除商品
     * @param ids 商品ID列表
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("list") List<Long> ids);
    
    /**
     * 条件查询商品列表
     * @param keyword 关键字
     * @param brandId 品牌ID
     * @param categoryId 分类ID
     * @param publishStatus 上架状态
     * @param pageable 分页参数
     * @return 商品分页列表
     */
    Page<PmsProducts> selectByCondition(
        @Param("keyword") String keyword, 
        @Param("brandId") Long brandId, 
        @Param("categoryId") Long categoryId, 
        @Param("publishStatus") Integer publishStatus, 
        Pageable pageable);
    
    /**
     * 批量更新商品上架状态
     * @param ids 商品ID列表
     * @param publishStatus 上架状态
     * @return 影响行数
     */
    int updatePublishStatus(@Param("list") List<Long> ids, @Param("publishStatus") Integer publishStatus);
    
    /**
     * 批量更新商品推荐状态
     * @param ids 商品ID列表
     * @param recommendStatus 推荐状态
     * @return 影响行数
     */
    int updateRecommendStatus(@Param("list") List<Long> ids, @Param("recommendStatus") Integer recommendStatus);
    
    /**
     * 批量更新商品新品状态
     * @param ids 商品ID列表
     * @param newStatus 新品状态
     * @return 影响行数
     */
    int updateNewStatus(@Param("list") List<Long> ids, @Param("newStatus") Integer newStatus);
    
    /**
     * 批量更新商品删除状态
     * @param ids 商品ID列表
     * @param deleteStatus 删除状态
     * @return 影响行数
     */
    int updateDeleteStatus(@Param("list") List<Long> ids, @Param("deleteStatus") Integer deleteStatus);
}
