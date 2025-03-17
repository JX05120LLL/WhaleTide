package com.whale_tide.service.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.management.product.ProductAttributeCategoryDto;
import com.whale_tide.dto.management.product.ProductAttributeCategoryParam;
import com.whale_tide.dto.management.product.ProductAttributeCategoryWithAttrDto;

import java.util.List;

/**
 * 商品属性分类管理Service
 */
public interface IProductAttributeCategoryService {
    
    /**
     * 创建属性分类
     */
    Long create(ProductAttributeCategoryParam productAttributeCategoryParam);

    /**
     * 修改属性分类
     */
    int update(Long id, ProductAttributeCategoryParam productAttributeCategoryParam);

    /**
     * 删除属性分类
     */
    int delete(Long id);

    /**
     * 获取属性分类详情
     */
    ProductAttributeCategoryDto getItem(Long id);

    /**
     * 分页查询属性分类
     */
    Page<ProductAttributeCategoryDto> getList(Integer pageNum, Integer pageSize);

    /**
     * 获取所有属性分类及其下属性
     */
    List<ProductAttributeCategoryWithAttrDto> getListWithAttr();
    

} 