package com.whale_tide.service.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.product.ProductNotFoundException;
import com.whale_tide.dto.client.product.*;
import com.whale_tide.entity.pms.*;
import com.whale_tide.mapper.pms.*;
import com.whale_tide.service.client.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户端商品服务实现类
 */
@Slf4j
@Service("clientProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private PmsProductsMapper pmsProductsMapper;
    @Autowired
    private PmsProductSkusMapper pmsProductSkusMapper;
    @Autowired
    private PmsProductCommentsMapper pmsProductCommentsMapper;
    @Autowired
    private PmsProductCategoriesMapper pmsProductCategoriesMapper;
    @Autowired
    private PmsProductImagesMapper pmsProductImagesMapper;
    @Autowired
    private PmsProductAttributesMapper pmsProductAttributesMapper;
    @Autowired
    private PmsBrandsMapper pmsBrandsMapper;

    /**
     * 商品关键词搜索
     *
     * @param request 搜索请求参数
     * @return 分页商品列表
     */
    @Override
    public PageResponse<ProductListItemResponse> getProductsByKeyword(ProductSearchRequest request) {
        // 参数验证
        if (request.getPageNum() == null || request.getPageNum() < 1) {
            request.setPageNum(1L);
        }
        if (request.getPageSize() == null || request.getPageSize() < 1) {
            request.setPageSize(10L);
        }
        
        // 解析参数
        String keyword = request.getKeyword();
        Long brandId = request.getBrandId();
        Long productCategoryId = request.getProductCategoryId();
        String sortField = request.getSortField();
        String order = request.getOrder();
        int pageNum = request.getPageNum().intValue();
        int pageSize = request.getPageSize().intValue();

        // 构建查询条件
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索：同时搜索商品名称和简介
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> 
                wrapper.like(PmsProducts::getName, keyword)
                    .or()
                    .like(PmsProducts::getBrief, keyword)
            );
        }
        
        // 品牌筛选
        if (brandId != null) {
            queryWrapper.eq(PmsProducts::getBrandId, brandId);
        }
        
        // 分类筛选
        if (productCategoryId != null) {
            queryWrapper.eq(PmsProducts::getCategoryId, productCategoryId);
        }
        
        // 排序处理
        if (StringUtils.hasText(sortField) && StringUtils.hasText(order)) {
            boolean isAsc = "asc".equalsIgnoreCase(order);
            
            // 根据不同的排序字段设置不同的排序方式
            switch (sortField) {
                case "price":
                    if (isAsc) queryWrapper.orderByAsc(PmsProducts::getPrice);
                    else queryWrapper.orderByDesc(PmsProducts::getPrice);
                    break;
                case "sale":
                    if (isAsc) queryWrapper.orderByAsc(PmsProducts::getSale);
                    else queryWrapper.orderByDesc(PmsProducts::getSale);
                    break;
                case "createTime":
                    if (isAsc) queryWrapper.orderByAsc(PmsProducts::getCreateTime);
                    else queryWrapper.orderByDesc(PmsProducts::getCreateTime);
                    break;
                default:
                    // 默认按照排序字段排序
                    if (isAsc) queryWrapper.orderByAsc(PmsProducts::getSort);
                    else queryWrapper.orderByDesc(PmsProducts::getSort);
            }
        } else {
            // 默认按照排序字段倒序排列
            queryWrapper.orderByDesc(PmsProducts::getSort);
        }

        // 分页查询
        Page<PmsProducts> page = new Page<>(pageNum, pageSize);
        IPage<PmsProducts> productPage = pmsProductsMapper.selectPage(page, queryWrapper);

        // 构建返回结果
        List<ProductListItemResponse> productList = productPage.getRecords().stream()
                .map(product -> {
                    ProductListItemResponse response = new ProductListItemResponse();
                    response.setId(product.getId());
                    response.setName(product.getName());
                    response.setPic(product.getMainImage());
                    response.setPrice(product.getPrice());
                    response.setSale(product.getSale());
                    return response;
                })
                .collect(Collectors.toList());

        // 计算总页数
        int totalPage = (int) Math.ceil((double) productPage.getTotal() / pageSize);

        // 封装分页结果
        return PageResponse.of(productList, pageNum, pageSize, productPage.getTotal(), totalPage);
    }

    /**
     * 获取商品分类树
     * 
     * @return 商品分类树
     */
    @Override
    public CategoryTreeResponse getCategoryTree() {
        // 创建响应对象
        CategoryTreeResponse response = new CategoryTreeResponse();
        
        // 查询所有分类
        LambdaQueryWrapper<PmsProductCategories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProductCategories::getStatus, 1); // 只查询启用状态的分类
        queryWrapper.orderByAsc(PmsProductCategories::getSort); // 按照排序字段排序
        List<PmsProductCategories> allCategories = pmsProductCategoriesMapper.selectList(queryWrapper);
        
        if (allCategories.isEmpty()) {
            return response;
        }
        
        // 按照父ID分组
        Map<Long, List<PmsProductCategories>> parentIdMap = allCategories.stream()
                .collect(Collectors.groupingBy(PmsProductCategories::getParentId));
        
        // 查找一级分类（父ID为0的分类）
        List<PmsProductCategories> rootCategories = parentIdMap.getOrDefault(0L, Collections.emptyList());
        
        // 如果有根分类，取第一个作为响应的基础信息
        if (!rootCategories.isEmpty()) {
            PmsProductCategories rootCategory = rootCategories.get(0);
            response.setId(rootCategory.getId());
            response.setName(rootCategory.getName());
            response.setLevel(rootCategory.getLevel());
            
            // 递归构建子分类
            List<CategoryTreeResponse> children = buildCategoryTree(rootCategories, parentIdMap);
            response.setChildren(children);
        }
        
        return response;
    }
    
    /**
     * 递归构建分类树
     * 
     * @param categories 当前层级的分类列表
     * @param parentIdMap 按父ID分组的分类Map
     * @return 分类树
     */
    private List<CategoryTreeResponse> buildCategoryTree(List<PmsProductCategories> categories, 
                                                        Map<Long, List<PmsProductCategories>> parentIdMap) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<CategoryTreeResponse> result = new ArrayList<>();
        
        for (PmsProductCategories category : categories) {
            CategoryTreeResponse node = new CategoryTreeResponse();
            node.setId(category.getId());
            node.setName(category.getName());
            node.setLevel(category.getLevel());
            
            // 递归查找子分类
            List<PmsProductCategories> children = parentIdMap.getOrDefault(category.getId(), Collections.emptyList());
            node.setChildren(buildCategoryTree(children, parentIdMap));
            
            result.add(node);
        }
        
        return result;
    }

    /**
     * 获取商品详情
     * 
     * @param id 商品ID
     * @return 商品详情
     */
    @Override
    @Transactional(readOnly = true) // 声明只读事务
    public ProductDetailResponse getProductDetail(Long id) {
        // 查询商品基本信息
        PmsProducts product = pmsProductsMapper.selectById(id);
        if (product == null) throw new ProductNotFoundException("商品不存在");
        
        // 查询商品图片
        LambdaQueryWrapper<PmsProductImages> imagesQuery = new LambdaQueryWrapper<>();
        imagesQuery.eq(PmsProductImages::getProductId, id);
        imagesQuery.orderByAsc(PmsProductImages::getSort);
        List<PmsProductImages> images = pmsProductImagesMapper.selectList(imagesQuery);
        
        // 主图和其他图片
        String mainImage = product.getMainImage(); // 主图
        
        // 查询品牌信息
        Long brandId = product.getBrandId();
        PmsBrands brand = pmsBrandsMapper.selectById(brandId);
        String brandName = brand != null ? brand.getName() : "";
        
        // 查询商品属性
        int attributeId = product.getAttributeId(); // 属性ID
        List<ProductDetailResponse.ProductAttribute> attributes = new ArrayList<>();
        
        if (attributeId > 0) {
            PmsProductAttributes attribute = pmsProductAttributesMapper.selectById(attributeId);
            if (attribute != null) {
                attributes.add(new ProductDetailResponse.ProductAttribute(
                    attribute.getName(), attribute.getInputOptions()));
            }
        }
        
        // 查询分类信息
        Long categoryId = product.getCategoryId();
        String categoryName = "";
        if (categoryId != null) {
            PmsProductCategories category = pmsProductCategoriesMapper.selectById(categoryId);
            if (category != null) {
                categoryName = category.getName();
            }
        }
        
        // 封装结果
        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPic(mainImage);
        response.setPrice(product.getPrice());
        response.setOriginalPrice(product.getOriginalPrice());
        response.setSale(product.getSale());
        response.setStock(product.getStock());
        response.setCategoryName(categoryName);
        response.setBrand(new ProductDetailResponse.BrandInfo(brandId, brandName));
        response.setAttributes(attributes);
        response.setDescription(product.getBrief());
        
        // 返回详情
        return response;
    }
}
