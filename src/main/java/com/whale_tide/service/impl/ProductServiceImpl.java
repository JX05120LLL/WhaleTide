package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.product.*;
import com.whale_tide.entity.PmsProducts;
import com.whale_tide.entity.PmsProduct;
import com.whale_tide.entity.PmsProductAttributeValues;
import com.whale_tide.entity.PmsProductSkus;
import com.whale_tide.mapper.PmsProductAttributeValuesMapper;
import com.whale_tide.mapper.PmsProductSkusMapper;
import com.whale_tide.mapper.PmsProductsMapper;
import com.whale_tide.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<PmsProductsMapper, PmsProducts> implements IProductService {
    @Autowired
    private PmsProductsMapper productsMapper;
    @Autowired
    private PmsProductSkusMapper skusMapper;
    @Autowired
    private PmsProductAttributeValuesMapper productAttributeValuesMapper;

    @Override
    public IPage<ProductListResult> getProductList(ProductQueryParam queryParam) {
        // 创建分页对象
        Page<PmsProducts> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        // 创建查询条件包装器
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        // 添加搜索关键词的模糊查询条件
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(PmsProducts::getName, queryParam.getKeyword());
        }
        // 添加品牌ID的精确查询条件
        if (queryParam.getBrandId()!= null) {
            queryWrapper.eq(PmsProducts::getBrandId, queryParam.getBrandId());
        }
        // 添加产品分类ID的精确查询条件
        if (queryParam.getProductCategoryId()!= null) {
            queryWrapper.eq(PmsProducts::getCategoryId, queryParam.getProductCategoryId());
        }
        // 添加上架状态的精确查询条件
        if (queryParam.getPublishStatus()!= null) {
            queryWrapper.eq(PmsProducts::getPublishStatus, queryParam.getPublishStatus());
        }
        // 执行查询
        IPage<PmsProducts> productPage = productsMapper.selectPage(page, queryWrapper);
        // 将查询结果转换为 ProductListResult 对象
        List<ProductListResult> resultList = productPage.getRecords().stream()
               .map(product -> {
                    ProductListResult result = new ProductListResult();
                    result.setId(product.getId());
                    result.setName(product.getName());
                    result.setPic(product.getMainImage());
                    result.setPrice(product.getPrice());
                    result.setSale(product.getSale());
                 //   result.setBrandName(product.());
                  //  result.setProductCategoryName(product.());
                    result.setPublishStatus(product.getPublishStatus());
                    result.setNewStatus(product.getNewStatus());
                    result.setRecommendStatus(product.getRecommendStatus());
                    return result;
                }).collect(Collectors.toList());
        // 返回分页结果
        return new Page<>();
    }

    @Override
    public ProductSimpleResult getProductSimple(String keyword) {
    // 创建查询条件包装器
    LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();

    // 添加搜索关键词的模糊查询条件
    if (StringUtils.isNotBlank(keyword)) {
        queryWrapper.like(PmsProducts::getName, keyword);
    }

    // 执行查询，获取产品列表
    List<PmsProducts> productList = productsMapper.selectList(queryWrapper);

    // 判断查询结果是否为空
    if (productList == null || productList.isEmpty()) {
        return null;
    }

    // 将查询结果转换为 ProductSimpleResult 对象
    ProductSimpleResult result = new ProductSimpleResult();
    PmsProducts product = productList.get(0); // 假设只返回第一个匹配的产品
    result.setId(product.getId());
    result.setName(product.getName());

    // 返回转换后的结果
    return result;
}

    @Override
    public int updateDeleteStatus(UpdateDeleteStatusParam updateDeleteStatusParam) {

        // 获取参数
        String ids = updateDeleteStatusParam.getIds();
        Integer deleteStatus = updateDeleteStatusParam.getDeleteStatus();

        // 解析参数
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        // 创建更新包装器
        UpdateWrapper<PmsProducts> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_deleted", deleteStatus).in("id", idList); // 设置更新条件

        // 执行更新
        return productsMapper.update(null, updateWrapper);   // 返回受影响的行数


    }

    @Override
    public int updateNewStatus(UpdateNewStatusParam updateNewStatusParam) {
        // 获取参数
        String ids = updateNewStatusParam.getIds();
        Integer newStatus = updateNewStatusParam.getNewStatus();

        // 解析参数  多个用逗号分隔
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        // 创建更新包装器
        UpdateWrapper<PmsProducts> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("new_status", newStatus).in("id", idList); // 设置更新条件

        // 执行更新
        return productsMapper.update(null, updateWrapper);   // 返回受影响的行数
    }

    @Override
    public int updateRecommendStatus(UpdateRecommendStatusParam updateRecommendStatusParam) {
        // 获取参数
        String ids = updateRecommendStatusParam.getIds();
        Integer recommendStatus = updateRecommendStatusParam.getRecommendStatus();

        // 解析参数  多个用逗号分隔
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        // 创建更新包装器
        UpdateWrapper<PmsProducts> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("recommend_status", recommendStatus).in("id", idList); // 设置更新条件

        // 执行更新
        return productsMapper.update(null, updateWrapper);   // 返回受影响的行数
    }

    @Override
    public int updatePublishStatusParam(UpdatePublishStatusParam updatePublishStatusParam) {
        // 获取参数
        String ids = updatePublishStatusParam.getIds();
        Integer publishStatus = updatePublishStatusParam.getPublishStatus();

        // 解析参数  多个用逗号分隔
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());        // 创建更新包装器
        UpdateWrapper<PmsProducts> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("publish_status", publishStatus).in("id", idList); // 设置更新条件

        // 执行更新
        return productsMapper.update(null, updateWrapper);   // 返回受影响的行数
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createProduct(ProductParam productParam) {
        int count = 0;
        
        // 1. 创建并保存商品基本信息
        PmsProducts product = new PmsProducts();
        ProductParam.ProductBasicParam basicParam = productParam.getProductParam();
        if (basicParam == null) {
            return 0;
        }

        // 复制基本信息
        BeanUtils.copyProperties(basicParam, product);
        
        // 设置分类ID字段映射
        product.setCategoryId(basicParam.getProductCategoryId());
        
        // 设置一些默认值
        if (StringUtils.isBlank(basicParam.getProductSn())) {
            product.setProductSn(generateProductSn()); // 生成商品编号
        }
        product.setVerifyStatus(0); // 默认未审核
        product.setSale(0); // 默认销量为0
        // 设置主图
        product.setMainImage(basicParam.getPic());
        product.setIsDeleted(0); // 默认未删除
        LocalDateTime now = LocalDateTime.now();
        product.setCreateTime(now);
        product.setUpdateTime(now);
        
        // 保存商品基本信息
        count = productsMapper.insert(product);
        if (count <= 0) {
            return 0;
        }
        
        // 获取插入后的商品ID
        Long productId = product.getId();
        
        // 2. 处理商品SKU
        if (productParam.getSkuStockList() != null && !productParam.getSkuStockList().isEmpty()) {
            List<PmsProductSkus> skuList = handleSkuList(productParam.getSkuStockList(), productId);
            // 批量保存SKU
            for (PmsProductSkus sku : skuList) {
                count += skusMapper.insert(sku);
            }
        }
        
        // 3. 处理商品属性值
        if (productParam.getProductAttributeValueList() != null && !productParam.getProductAttributeValueList().isEmpty()) {
            List<PmsProductAttributeValues> attributeValueList = handleAttributeList(productParam.getProductAttributeValueList(), productId);
            // 批量保存属性值
            for (PmsProductAttributeValues attributeValue : attributeValueList) {
                count += productAttributeValuesMapper.insert(attributeValue);
            }
        }

        
        return count;
    }
    
    /**
     * 生成商品编号
     */
    private String generateProductSn() {
        // 简单实现，使用时间戳生成商品编号
        return "P" + System.currentTimeMillis();
    }
    
    /**
     * 处理商品SKU列表
     */
    private List<PmsProductSkus> handleSkuList(List<ProductParam.SkuStockParam> skuParamList, Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return skuParamList.stream().map(skuParam -> {
            PmsProductSkus sku = new PmsProductSkus();
            BeanUtils.copyProperties(skuParam, sku);
            sku.setProductId(productId);
            sku.setSkuCode(skuParam.getSkuCode() != null && !skuParam.getSkuCode().isEmpty() ? 
                    skuParam.getSkuCode() : generateSkuCode(productId));
            sku.setImage(skuParam.getPic()); // 映射图片字段
            sku.setSpecs(skuParam.getSpData()); // 映射规格数据
            sku.setLowStock(10); // 默认预警库存
            sku.setSale(0); // 默认销量
            sku.setLockStock(0); // 默认锁定库存
            sku.setCreateTime(now);
            sku.setUpdateTime(now);
            return sku;
        }).collect(Collectors.toList());
    }
    
    /**
     * 处理商品属性列表
     */
    private List<PmsProductAttributeValues> handleAttributeList(List<ProductParam.ProductAttributeValueParam> attributeParamList, Long productId) {
        List<PmsProductAttributeValues> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (ProductParam.ProductAttributeValueParam attributeParam : attributeParamList) {
            PmsProductAttributeValues attributeValue = new PmsProductAttributeValues();
            attributeValue.setProductId(productId);
            attributeValue.setAttributeId(attributeParam.getProductAttributeId());
            attributeValue.setValue(attributeParam.getValue());
            attributeValue.setCreateTime(now);
            result.add(attributeValue);
        }
        
        return result;
    }
    
    /**
     * 生成SKU编码
     */
    private String generateSkuCode(Long productId) {
        // 简单实现，使用商品ID+时间戳生成SKU编码
        return "SKU" + productId + System.currentTimeMillis() % 10000;
    }
}
