package com.whale_tide.service.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.*;
import com.whale_tide.entity.pms.*;
import com.whale_tide.mapper.pms.*;
import com.whale_tide.service.client.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    // 商品关键词搜索
    @Override
    public PageResponse<ProductListItemResponse> getProductsByKeyword(ProductSearchRequest request) {
        // 解析参数
        String keyword = request.getKeyword();
        Long brandId = request.getBrandId();
        Long productCategoryId = request.getProductCategoryId();
        String sortField = request.getSortField();
        String order = request.getOrder();// 排序方式
        int pageNum = request.getPageNum().intValue();  // 转换为int类型
        int pageSize = request.getPageSize().intValue(); // 转换为int类型

        // 构建查询条件
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(PmsProducts::getName, keyword);
        if (brandId != null)  queryWrapper.eq(PmsProducts::getBrandId, brandId);
        if (productCategoryId != null)  queryWrapper.eq(PmsProducts::getCategoryId, productCategoryId);
        if (sortField != null && order != null) {
            if (order.equalsIgnoreCase("asc")) {
                queryWrapper.orderByAsc(PmsProducts::getSort);
            } else {
                queryWrapper.orderByDesc(PmsProducts::getSort);
            }
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
     * @return
     */
    @Override
    public CategoryTreeResponse getCategoryTree() {


        return null;
    }

    /**
     * 获取商品详情
     * @param id
     * @return ProductDetailResponse
     */
    @Override
    public ProductDetailResponse getProductDetail(Long id) {
        // 查询商品基本信息
        PmsProducts product = pmsProductsMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        // 查询商品图片
        LambdaQueryWrapper<PmsProductImages> eq = new LambdaQueryWrapper<PmsProductImages>().eq(PmsProductImages::getProductId, id);
        String mainImage = product.getMainImage();// 主图
        String imageUrl = pmsProductImagesMapper.selectOne(eq).getImageUrl();

        // 查询品牌信息
        Long brandId = product.getBrandId();
        LambdaQueryWrapper<PmsBrands> bq = new LambdaQueryWrapper<PmsBrands>().eq(PmsBrands::getId, brandId);
        String brandName = pmsBrandsMapper.selectOne(bq).getName();

        // 查询商品属性
        int attributeId = product.getAttributeId(); // 属性ID
        LambdaQueryWrapper<PmsProductAttributes> aq = new LambdaQueryWrapper<PmsProductAttributes>();
        aq.eq(PmsProductAttributes::getId, attributeId);
        PmsProductAttributes attribute = pmsProductAttributesMapper.selectOne(aq);

        // 封装商品属性
        List<ProductDetailResponse.ProductAttribute> attributes = new ArrayList<>();
        if (attribute != null) {
            attributes.add(new ProductDetailResponse.ProductAttribute(attribute.getName(), attribute.getInputOptions()));
        }

        // 封装结果
        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(product.getId());
        response.setName(product.getName());// 商品名称
        response.setPic(mainImage);// 商品图片
        response.setPrice(product.getPrice());// 商品价格
        response.setOriginalPrice(product.getOriginalPrice());//
        response.setSale(product.getSale());// 商品销量
        response.setStock(product.getStock());// 商品库存
        response.setBrand(new ProductDetailResponse.BrandInfo(brandId, brandName));
        response.setAttributes(attributes); // 设置商品属性
        response.setDescription(product.getBrief());// 商品描述

        return response;
    }




}
