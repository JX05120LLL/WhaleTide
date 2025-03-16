package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.product.*;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.entity.pms.PmsProductAttributeValues;
import com.whale_tide.entity.pms.PmsProductSkus;
import com.whale_tide.mapper.pms.PmsProductAttributeValuesMapper;
import com.whale_tide.mapper.pms.PmsProductSkusMapper;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.mapper.pms.PmsProductCategoriesMapper;
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
    @Autowired
    private PmsProductCategoriesMapper productCategoriesMapper;

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
        
        // 验证分类ID是否有效，如果无效则使用默认值1
        Long categoryId = basicParam.getProductCategoryId();
        if (categoryId == null || categoryId == 0 || !isValidCategoryId(categoryId)) {
            log.warn("商品分类ID无效，将使用默认分类ID：1");
            product.setCategoryId(1L); // 使用ID为1的分类
        } else {
            product.setCategoryId(categoryId);
        }
        
        // 设置一些默认值
        if (StringUtils.isBlank(basicParam.getProductSn())) {
            product.setProductSn(generateProductSn()); // 生成商品编号
        }
        product.setVerifyStatus(0); // 默认未审核
        product.setSale(0); // 默认销量为0
        
        // 设置商户ID - 优先从请求参数获取，如果没有则设置默认值
        if (basicParam.getMerchantId() != null) {
            // 如果请求中有商户ID，则使用请求中的商户ID
            product.setMerchantId(basicParam.getMerchantId());
        } else {
            // 如果请求中没有商户ID，则设置为默认值1
            // 注意: 实际项目中应该从当前登录用户获取，这里使用默认值仅用于开发阶段
            product.setMerchantId(1L);
            log.warn("商品创建时未提供商户ID，使用默认值：1");
        }
        
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

    /**
     * 检查分类ID是否存在于数据库中
     */
    private boolean isValidCategoryId(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return false;
        }
        
        try {
            // 查询该ID是否在分类表中存在
            return productCategoriesMapper.selectById(categoryId) != null;
        } catch (Exception e) {
            log.error("检查分类ID时发生错误", e);
            return false;
        }
    }

    /**
     * 获取商品编辑信息
     */
    @Override
    public ProductParam getUpdateInfo(Long id) {
        try {
            // 查询商品基本信息
            PmsProducts product = productsMapper.selectById(id);
            if (product == null) {
                return null;
            }
            
            // 创建返回对象
            ProductParam productParam = new ProductParam();
            BeanUtils.copyProperties(product, productParam);
            
            // 查询商品SKU信息
            LambdaQueryWrapper<PmsProductSkus> skuQueryWrapper = new LambdaQueryWrapper<>();
            skuQueryWrapper.eq(PmsProductSkus::getProductId, id);
            List<PmsProductSkus> skuList = skusMapper.selectList(skuQueryWrapper);
            
            // 转换SKU信息
            if (skuList != null && !skuList.isEmpty()) {
                List<ProductParam.SkuStockParam> skuStockParams = new ArrayList<>();
                for (PmsProductSkus sku : skuList) {
                    ProductParam.SkuStockParam skuStockParam = new ProductParam.SkuStockParam();
                    BeanUtils.copyProperties(sku, skuStockParam);
                    skuStockParams.add(skuStockParam);
                }
                productParam.setSkuStockList(skuStockParams);
            }
            
            // 查询商品属性信息
            LambdaQueryWrapper<PmsProductAttributeValues> attributeQueryWrapper = new LambdaQueryWrapper<>();
            attributeQueryWrapper.eq(PmsProductAttributeValues::getProductId, id);
            List<PmsProductAttributeValues> attributeList = productAttributeValuesMapper.selectList(attributeQueryWrapper);
            
            // 转换属性信息
            if (attributeList != null && !attributeList.isEmpty()) {
                List<ProductParam.ProductAttributeValueParam> attributeValueParams = new ArrayList<>();
                for (PmsProductAttributeValues attribute : attributeList) {
                    ProductParam.ProductAttributeValueParam attributeValueParam = new ProductParam.ProductAttributeValueParam();
                    attributeValueParam.setProductAttributeId(attribute.getAttributeId());
                    attributeValueParam.setValue(attribute.getValue());
                    attributeValueParams.add(attributeValueParam);
                }
                productParam.setProductAttributeValueList(attributeValueParams);
            }
            
            return productParam;
        } catch (Exception e) {
            log.error("获取商品编辑信息时发生错误", e);
            return null;
        }
    }

    /**
     * 更新商品信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProduct(Long id, ProductParam productParam) {
        try {
            // 查询商品是否存在
            PmsProducts existingProduct = productsMapper.selectById(id);
            if (existingProduct == null) {
                return 0;
            }
            
            // 更新商品基本信息
            PmsProducts product = new PmsProducts();
            BeanUtils.copyProperties(productParam, product);
            product.setId(id);
            product.setUpdateTime(LocalDateTime.now());
            
            // 执行更新
            int count = productsMapper.updateById(product);
            if (count <= 0) {
                return 0;
            }
            
            // 删除原有SKU信息
            LambdaQueryWrapper<PmsProductSkus> skuQueryWrapper = new LambdaQueryWrapper<>();
            skuQueryWrapper.eq(PmsProductSkus::getProductId, id);
            skusMapper.delete(skuQueryWrapper);
            
            // 添加新的SKU信息
            List<PmsProductSkus> skuList = handleSkuList(productParam.getSkuStockList(), id);
            for (PmsProductSkus sku : skuList) {
                skusMapper.insert(sku);
            }
            
            // 删除原有属性信息
            LambdaQueryWrapper<PmsProductAttributeValues> attributeQueryWrapper = new LambdaQueryWrapper<>();
            attributeQueryWrapper.eq(PmsProductAttributeValues::getProductId, id);
            productAttributeValuesMapper.delete(attributeQueryWrapper);
            
            // 添加新的属性信息
            List<PmsProductAttributeValues> attributeList = handleAttributeList(productParam.getProductAttributeValueList(), id);
            for (PmsProductAttributeValues attribute : attributeList) {
                productAttributeValuesMapper.insert(attribute);
            }
            
            return 1;
        } catch (Exception e) {
            log.error("更新商品信息时发生错误", e);
            throw e;
        }
    }
}
