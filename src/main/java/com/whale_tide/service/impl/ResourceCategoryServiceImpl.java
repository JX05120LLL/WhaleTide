package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.resource.ResourceCategoryParam;
import com.whale_tide.dto.resource.ResourceCategoryResult;
import com.whale_tide.entity.ams.AmsResourceCategories;
import com.whale_tide.mapper.ams.AmsResourceCategoriesMapper;
import com.whale_tide.service.IResourceCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源分类管理Service实现类
 */
@Slf4j
@Service
public class ResourceCategoryServiceImpl extends ServiceImpl<AmsResourceCategoriesMapper, AmsResourceCategories> implements IResourceCategoryService {

    @Override
    public List<ResourceCategoryResult> listAll() {
        // 查询所有资源分类
        LambdaQueryWrapper<AmsResourceCategories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(AmsResourceCategories::getSort);
        List<AmsResourceCategories> categoriesList = list(queryWrapper);
        
        // 转换结果
        return categoriesList.stream().map(category -> {
            ResourceCategoryResult result = new ResourceCategoryResult();
            BeanUtils.copyProperties(category, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean create(ResourceCategoryParam resourceCategoryParam) {
        AmsResourceCategories category = new AmsResourceCategories();
        BeanUtils.copyProperties(resourceCategoryParam, category);
        
        // 设置创建时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        
        return save(category);
    }

    @Override
    public boolean update(Long id, ResourceCategoryParam resourceCategoryParam) {
        // 查询分类是否存在
        AmsResourceCategories category = getById(id);
        if (category == null) {
            return false;
        }
        
        // 更新分类信息
        BeanUtils.copyProperties(resourceCategoryParam, category);
        category.setUpdateTime(LocalDateTime.now());
        
        return updateById(category);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }
} 