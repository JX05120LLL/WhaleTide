package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.resource.ResourceParam;
import com.whale_tide.dto.resource.ResourceResult;
import com.whale_tide.entity.AmsResources;
import com.whale_tide.mapper.AmsResourcesMapper;
import com.whale_tide.service.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源管理Service实现类
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<AmsResourcesMapper, AmsResources> implements IResourceService {

    @Override
    public IPage<ResourceResult> list(String nameKeyword, String urlKeyword, Long categoryId, Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<AmsResources> page = new Page<>(pageNum, pageSize);
        
        // 构建查询条件
        LambdaQueryWrapper<AmsResources> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加名称关键字查询条件
        if (!StringUtils.isEmpty(nameKeyword)) {
            queryWrapper.like(AmsResources::getName, nameKeyword);
        }
        
        // 添加URL关键字查询条件
        if (!StringUtils.isEmpty(urlKeyword)) {
            queryWrapper.like(AmsResources::getUrl, urlKeyword);
        }
        
        // 添加分类ID查询条件
        if (categoryId != null && categoryId > 0) {
            queryWrapper.eq(AmsResources::getCategoryId, categoryId);
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(AmsResources::getCreateTime);
        
        // 执行查询
        IPage<AmsResources> resourcesPage = page(page, queryWrapper);
        
        // 创建结果页对象
        Page<ResourceResult> resultPage = new Page<>(pageNum, pageSize);
        resultPage.setTotal(resourcesPage.getTotal());
        
        // 转换结果
        List<ResourceResult> resultList = resourcesPage.getRecords().stream().map(resource -> {
            ResourceResult result = new ResourceResult();
            BeanUtils.copyProperties(resource, result);
            return result;
        }).collect(Collectors.toList());
        
        resultPage.setRecords(resultList);
        
        return resultPage;
    }

    @Override
    public List<ResourceResult> listAll() {
        // 查询所有资源
        List<AmsResources> resourcesList = list();
        
        // 转换结果
        return resourcesList.stream().map(resource -> {
            ResourceResult result = new ResourceResult();
            BeanUtils.copyProperties(resource, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean create(ResourceParam resourceParam) {
        AmsResources resource = new AmsResources();
        BeanUtils.copyProperties(resourceParam, resource);
        
        // 设置创建时间
        resource.setCreateTime(LocalDateTime.now());
        resource.setUpdateTime(LocalDateTime.now());
        
        // 设置默认状态为启用
        resource.setStatus(1);
        
        return save(resource);
    }

    @Override
    public boolean update(Long id, ResourceParam resourceParam) {
        // 查询资源是否存在
        AmsResources resource = getById(id);
        if (resource == null) {
            return false;
        }
        
        // 更新资源信息
        BeanUtils.copyProperties(resourceParam, resource);
        resource.setUpdateTime(LocalDateTime.now());
        
        return updateById(resource);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public ResourceResult getItem(Long id) {
        AmsResources resource = getById(id);
        if (resource == null) {
            return null;
        }
        
        ResourceResult result = new ResourceResult();
        BeanUtils.copyProperties(resource, result);
        
        return result;
    }
} 