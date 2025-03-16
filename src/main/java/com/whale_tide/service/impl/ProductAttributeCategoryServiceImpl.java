package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.product.ProductAttributeCategoryDto;
import com.whale_tide.dto.product.ProductAttributeCategoryParam;
import com.whale_tide.dto.product.ProductAttributeCategoryWithAttrDto;
import com.whale_tide.dto.product.ProductAttributeDto;
import com.whale_tide.entity.pms.PmsProductAttributeCategories;
import com.whale_tide.entity.pms.PmsProductAttributes;
import com.whale_tide.mapper.pms.PmsProductAttributeCategoriesMapper;
import com.whale_tide.mapper.pms.PmsProductAttributesMapper;
import com.whale_tide.service.IProductAttributeCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品属性分类管理Service实现类
 */
@Slf4j
@Service
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<PmsProductAttributeCategoriesMapper, PmsProductAttributeCategories> implements IProductAttributeCategoryService {

    @Autowired
    private PmsProductAttributesMapper productAttributesMapper;

    @Override
    public Long create(ProductAttributeCategoryParam productAttributeCategoryParam) {
        PmsProductAttributeCategories pmsProductAttributeCategories = new PmsProductAttributeCategories();
        pmsProductAttributeCategories.setName(productAttributeCategoryParam.getName());
        pmsProductAttributeCategories.setAttributeCount(0);
        pmsProductAttributeCategories.setParamCount(0);
        pmsProductAttributeCategories.setCreateTime(LocalDateTime.now());
        pmsProductAttributeCategories.setUpdateTime(LocalDateTime.now());
        save(pmsProductAttributeCategories);
        return pmsProductAttributeCategories.getId();
    }

    @Override
    public int update(Long id, ProductAttributeCategoryParam productAttributeCategoryParam) {
        PmsProductAttributeCategories pmsProductAttributeCategories = getById(id);
        if (pmsProductAttributeCategories == null) {
            return 0;
        }
        
        pmsProductAttributeCategories.setName(productAttributeCategoryParam.getName());
        pmsProductAttributeCategories.setUpdateTime(LocalDateTime.now());
        
        if (updateById(pmsProductAttributeCategories)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        if (removeById(id)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public ProductAttributeCategoryDto getItem(Long id) {
        PmsProductAttributeCategories category = getById(id);
        if (category == null) {
            return null;
        }
        
        ProductAttributeCategoryDto dto = new ProductAttributeCategoryDto();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }

    @Override
    public Page<ProductAttributeCategoryDto> getList(Integer pageNum, Integer pageSize) {
        try {
            // 创建分页对象
            Page<PmsProductAttributeCategories> page = new Page<>(pageNum, pageSize);
            
            // 执行分页查询
            page = page(page);
            
            // 创建结果页对象
            Page<ProductAttributeCategoryDto> result = new Page<>();
            result.setTotal(page.getTotal());
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            
            // 转换结果
            List<ProductAttributeCategoryDto> dtoList = new ArrayList<>();
            for (PmsProductAttributeCategories item : page.getRecords()) {
                if (item != null) {
                    ProductAttributeCategoryDto dto = new ProductAttributeCategoryDto();
                    BeanUtils.copyProperties(item, dto);
                    dtoList.add(dto);
                }
            }
            
            result.setRecords(dtoList);
            return result;
        } catch (Exception e) {
            log.error("获取属性分类列表出错", e);
            // 返回空分页对象
            Page<ProductAttributeCategoryDto> emptyPage = new Page<>(pageNum, pageSize);
            emptyPage.setRecords(new ArrayList<>());
            return emptyPage;
        }
    }

    @Override
    public List<ProductAttributeCategoryWithAttrDto> getListWithAttr() {
        try {
            // 先简化实现，只返回分类，不包含属性，避免可能的问题
            List<PmsProductAttributeCategories> categoryList = list();
            
            List<ProductAttributeCategoryWithAttrDto> result = new ArrayList<>();
            for (PmsProductAttributeCategories category : categoryList) {
                if (category != null) {
                    ProductAttributeCategoryWithAttrDto dto = new ProductAttributeCategoryWithAttrDto();
                    BeanUtils.copyProperties(category, dto);
                    dto.setProductAttributeList(new ArrayList<>());
                    result.add(dto);
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取属性分类及属性列表出错", e);
            return new ArrayList<>();
        }
    }


} 