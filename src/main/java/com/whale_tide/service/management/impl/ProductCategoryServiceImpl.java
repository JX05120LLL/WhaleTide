package com.whale_tide.service.management.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.management.product.ProductCategoryDto;
import com.whale_tide.dto.management.product.ProductCategoryParam;
import com.whale_tide.entity.pms.PmsProductCategories;
import com.whale_tide.mapper.pms.PmsProductCategoriesMapper;
import com.whale_tide.service.management.IProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Service
@Slf4j
public class ProductCategoryServiceImpl implements IProductCategoryService {

    @Autowired
    private PmsProductCategoriesMapper productCategoriesMapper;

    @Override
    public Page<ProductCategoryDto> getList(Long parentId, Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<PmsProductCategories> page = new Page<>(pageNum, pageSize);
        
        // 创建查询条件
        LambdaQueryWrapper<PmsProductCategories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProductCategories::getParentId, parentId)
                    .orderByAsc(PmsProductCategories::getSort);
        
        // 执行查询
        Page<PmsProductCategories> categoryPage = productCategoriesMapper.selectPage(page, queryWrapper);
        
        // 转换结果
        List<ProductCategoryDto> categoryDtoList = categoryPage.getRecords().stream()
                .map(this::convertToCategoryDto)
                .collect(Collectors.toList());
        
        // 创建返回的分页对象
        Page<ProductCategoryDto> resultPage = new Page<>(categoryPage.getCurrent(), categoryPage.getSize(), categoryPage.getTotal());
        resultPage.setRecords(categoryDtoList);
        
        return resultPage;
    }

    @Override
    public List<Map<String, Object>> getListWithChildren() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询所有一级分类
        LambdaQueryWrapper<PmsProductCategories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProductCategories::getParentId, 0)
                    .orderByAsc(PmsProductCategories::getSort);
        
        List<PmsProductCategories> parentCategories = productCategoriesMapper.selectList(queryWrapper);
        
        // 为每个一级分类查询其子分类
        for (PmsProductCategories parentCategory : parentCategories) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", parentCategory.getId());
            categoryMap.put("name", parentCategory.getName());
            
            // 查询子分类
            LambdaQueryWrapper<PmsProductCategories> childQueryWrapper = new LambdaQueryWrapper<>();
            childQueryWrapper.eq(PmsProductCategories::getParentId, parentCategory.getId())
                            .orderByAsc(PmsProductCategories::getSort);
            
            List<PmsProductCategories> childCategories = productCategoriesMapper.selectList(childQueryWrapper);
            
            // 转换子分类为简单Map对象
            List<Map<String, Object>> children = childCategories.stream().map(child -> {
                Map<String, Object> childMap = new HashMap<>();
                childMap.put("id", child.getId());
                childMap.put("name", child.getName());
                return childMap;
            }).collect(Collectors.toList());
            
            categoryMap.put("children", children);
            result.add(categoryMap);
        }
        
        return result;
    }

    @Override
    public ProductCategoryDto getItem(Long id) {
        PmsProductCategories category = productCategoriesMapper.selectById(id);
        if (category == null) {
            return null;
        }
        return convertToCategoryDto(category);
    }

    @Override
    public Long create(ProductCategoryParam productCategoryParam) {
        PmsProductCategories category = new PmsProductCategories();
        BeanUtils.copyProperties(productCategoryParam, category);
        
        // 设置默认值
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        // 初始产品数量为0 - 如果实体类没有setProductCount方法，则跳过
        // category.setProductCount(0);
        
        productCategoriesMapper.insert(category);
        return category.getId();
    }

    @Override
    public int update(Long id, ProductCategoryParam productCategoryParam) {
        PmsProductCategories category = productCategoriesMapper.selectById(id);
        if (category == null) {
            return 0;
        }
        
        BeanUtils.copyProperties(productCategoryParam, category);
        category.setId(id); // 确保ID不被覆盖
        category.setUpdateTime(LocalDateTime.now());
        
        return productCategoriesMapper.updateById(category);
    }

    @Override
    public int delete(Long id) {
        // 检查是否有子分类
        LambdaQueryWrapper<PmsProductCategories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProductCategories::getParentId, id);
        long count = productCategoriesMapper.selectCount(queryWrapper);
        
        if (count > 0) {
            return -1; // 有子分类，不能删除
        }
        
        return productCategoriesMapper.deleteById(id);
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        UpdateWrapper<PmsProductCategories> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("nav_status", navStatus)
                    .in("id", ids);
        
        return productCategoriesMapper.update(null, updateWrapper);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        UpdateWrapper<PmsProductCategories> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("show_status", showStatus)
                    .in("id", ids);
        
        return productCategoriesMapper.update(null, updateWrapper);
    }

    /**
     * 将PmsProductCategories实体转换为ProductCategoryDto对象
     */
    private ProductCategoryDto convertToCategoryDto(PmsProductCategories category) {
        ProductCategoryDto categoryDto = new ProductCategoryDto();
        BeanUtils.copyProperties(category, categoryDto);
        return categoryDto;
    }
} 