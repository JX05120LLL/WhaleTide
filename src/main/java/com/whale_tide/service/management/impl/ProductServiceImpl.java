package com.whale_tide.service.management.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.management.product.*;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.entity.pms.PmsProductAttributeValues;
import com.whale_tide.entity.pms.PmsProductSkus;
import com.whale_tide.entity.pms.PmsProductCategories;
import com.whale_tide.entity.pms.PmsBrands;
import com.whale_tide.mapper.pms.PmsProductAttributeValuesMapper;
import com.whale_tide.mapper.pms.PmsProductCategoriesMapper;
import com.whale_tide.mapper.pms.PmsProductSkusMapper;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.mapper.pms.PmsBrandsMapper;
import com.whale_tide.service.management.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理服务实现类
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    private PmsProductsMapper productsMapper;
    @Autowired
    private PmsProductSkusMapper skusMapper;
    @Autowired
    private PmsProductAttributeValuesMapper productAttributeValuesMapper;
    @Autowired
    private PmsProductCategoriesMapper productCategoriesMapper;
    @Autowired
    private PmsBrandsMapper brandsMapper;
    @Autowired
    private PmsProductSkusMapper productSkusMapper;

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
        if (queryParam.getBrandId() != null) {
            queryWrapper.eq(PmsProducts::getBrandId, queryParam.getBrandId());
        }
        // 添加产品分类ID的精确查询条件
        if (queryParam.getProductCategoryId() != null) {
            queryWrapper.eq(PmsProducts::getCategoryId, queryParam.getProductCategoryId());
        }
        // 添加上架状态的精确查询条件
        if (queryParam.getPublishStatus() != null) {
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

                    // 设置品牌名称 - 需要根据品牌ID查询品牌名称
                    if (product.getBrandId() != null) {
                        try {
                            // 根据brandId查询品牌名称，这里假设有一个getBrandById方法
                            // 如果没有直接的方法，可以先使用ID显示
                            PmsBrands brand = brandsMapper.selectById(product.getBrandId());
                            result.setBrandName(brand != null ? brand.getName() : "品牌ID: " + product.getBrandId());
                        } catch (Exception e) {
                            log.error("获取品牌名称失败: {}", e.getMessage());
                            result.setBrandName("");
                        }
                    }

                    // 设置产品分类名称 - 需要根据分类ID查询分类名称
                    if (product.getCategoryId() != null) {
                        try {
                            // 通过productCategoriesMapper查询分类信息
                            PmsProductCategories category = productCategoriesMapper.selectById(product.getCategoryId());
                            result.setProductCategoryName(category != null ? category.getName() : "分类ID: " + product.getCategoryId());
                        } catch (Exception e) {
                            log.error("获取产品分类名称失败: {}", e.getMessage());
                            result.setProductCategoryName("");
                        }
                    }

                    result.setPublishStatus(product.getPublishStatus());
                    result.setNewStatus(product.getNewStatus());
                    result.setRecommendStatus(product.getRecommendStatus());
                    return result;
                }).collect(Collectors.toList());

        // 创建并返回包含真实数据的分页对象
        Page<ProductListResult> resultPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        resultPage.setRecords(resultList);
        return resultPage;
    }

    @Override
    public List<ProductSimpleResult> getProductSimple(String keyword) {
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
            return new ArrayList<>();
        }

        // 将查询结果转换为 ProductSimpleResult 对象
        List<ProductSimpleResult> resultList = productList.stream()
                .map(product -> {
                    ProductSimpleResult result = new ProductSimpleResult();
                    result.setId(product.getId());
                    result.setName(product.getName());
                    return result;
                })
                .collect(Collectors.toList());

        return resultList;
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

        // 创建并保存商品基本信息
        PmsProducts product = new PmsProducts();
        
        // 处理商品基本信息可能为空的情况
        if (productParam.getProductParam() == null) {
            log.info("创建商品时基本参数为空，直接从外层对象获取");
            // 直接从外层对象复制属性
            BeanUtils.copyProperties(productParam, product);
            
            // 处理前端可能使用productCategoryId而不是categoryId的情况
            try {
                // 使用反射获取productCategoryId字段值
                java.lang.reflect.Field field = productParam.getClass().getDeclaredField("productCategoryId");
                field.setAccessible(true);
                Object value = field.get(productParam);
                if (value != null && product.getCategoryId() == null) {
                    log.info("从productCategoryId字段获取值: {}", value);
                    if (value instanceof Number) {
                        product.setCategoryId(((Number) value).longValue());
                    }
                }
            } catch (Exception e) {
                log.warn("处理productCategoryId字段时出错", e);
            }
            
            // 处理前端可能使用pic而不是mainImage的情况
            try {
                java.lang.reflect.Field picField = productParam.getClass().getDeclaredField("pic");
                picField.setAccessible(true);
                Object picValue = picField.get(productParam);
                if (picValue != null && picValue instanceof String && StringUtils.isNotBlank((String) picValue)) {
                    log.info("从pic字段获取值: {}", picValue);
                    product.setMainImage((String) picValue);
                }
            } catch (Exception e) {
                log.warn("处理pic字段时出错", e);
            }
        } else {
            // 正常从嵌套结构中获取参数
            ProductParam.ProductBasicParam basicParam = productParam.getProductParam();
            BeanUtils.copyProperties(basicParam, product);
            
            // 确保主图字段正确设置
            if (StringUtils.isNotBlank(basicParam.getPic())) {
                product.setMainImage(basicParam.getPic());
            }
        }

        // 确保重要字段已经设置了值
        if (StringUtils.isBlank(product.getName())) {
            product.setName("未命名商品_" + System.currentTimeMillis());
            log.info("商品名称为空，设置默认值: {}", product.getName());
        }
        
        // 确保商品分类ID已设置
        if (product.getCategoryId() == null) {
            product.setCategoryId(1L); // 默认分类ID
            log.info("商品分类ID为空，设置默认值: 1");
        }
        
        // 确保价格已设置
        if (product.getPrice() == null) {
            product.setPrice(new BigDecimal("0.01"));
            log.info("商品价格为空，设置默认值: 0.01");
        }
        
        // 确保库存已设置
        if (product.getStock() == null) {
            product.setStock(100);
            log.info("商品库存为空，设置默认值: 100");
        }

        // 设置必要的默认值 - 开始
        // 设置商品基础默认值
        product.setAttributeId(0); // 设置默认属性ID
        product.setVerifyStatus(0); // 默认未审核
        product.setSale(0); // 默认销量为0
        product.setIsDeleted(0); // 默认未删除
        
        // 处理可能为空的字段
        if (product.getPublishStatus() == null) {
            product.setPublishStatus(0); // 默认下架状态
        }
        
        if (product.getNewStatus() == null) {
            product.setNewStatus(0); // 默认非新品
        }
        
        if (product.getRecommendStatus() == null) {
            product.setRecommendStatus(0); // 默认不推荐
        }

        // 设置商户ID (如果为空)
        if (product.getMerchantId() == null) {
            log.info("商户ID为空，设置默认值1");
            product.setMerchantId(1L);
        }

        // 生成商品编号 (如果为空)
        if (StringUtils.isBlank(product.getProductSn())) {
            log.info("商品编号为空，自动生成");
            product.setProductSn(generateProductSn());
        }

        // 设置时间
        LocalDateTime now = LocalDateTime.now();
        product.setCreateTime(now);
        product.setUpdateTime(now);
        // 设置必要的默认值 - 结束

        log.info("准备保存商品基本信息: {}", product);
        
        // 保存商品基本信息
        try {
            count = productsMapper.insert(product);
            log.info("商品基本信息保存成功，影响行数: {}", count);
        } catch (Exception e) {
            log.error("保存商品基本信息失败", e);
            throw e;
        }
        
        if (count <= 0) {
            log.error("保存商品基本信息失败，影响行数为0");
            return 0;
        }

        // 获取插入后的商品ID
        Long productId = product.getId();
        log.info("商品基本信息保存成功，ID: {}", productId);

        // 处理商品SKU - 如果为空则创建默认SKU
        if (productParam.getSkuStockList() == null || productParam.getSkuStockList().isEmpty()) {
            log.info("SKU列表为空，创建默认SKU");
            // 创建默认SKU
            PmsProductSkus defaultSku = new PmsProductSkus();
            defaultSku.setProductId(productId);
            defaultSku.setSkuCode(generateSkuCode(productId));
            defaultSku.setPrice(product.getPrice());
            defaultSku.setStock(product.getStock());
            defaultSku.setLowStock(10);
            defaultSku.setSale(0);
            defaultSku.setLockStock(0);
            defaultSku.setSpecs("{}"); // 空规格
            defaultSku.setCreateTime(now);
            defaultSku.setUpdateTime(now);
            
            try {
                count += skusMapper.insert(defaultSku);
                log.info("默认SKU创建成功");
            } catch (Exception e) {
                log.error("创建默认SKU失败", e);
                throw e;
            }
        } else {
            // 处理提供的SKU列表
            log.info("处理提供的SKU列表，数量: {}", productParam.getSkuStockList().size());
            List<PmsProductSkus> skuList = handleSkuList(productParam.getSkuStockList(), productId);
            // 批量保存SKU
            for (PmsProductSkus sku : skuList) {
                try {
                    count += skusMapper.insert(sku);
                } catch (Exception e) {
                    log.error("保存SKU失败: {}", sku, e);
                    throw e;
                }
            }
        }

        // 处理商品属性值 - 可选项，不影响商品创建
        if (productParam.getProductAttributeValueList() != null && !productParam.getProductAttributeValueList().isEmpty()) {
            log.info("处理商品属性值，数量: {}", productParam.getProductAttributeValueList().size());
            try {
                List<PmsProductAttributeValues> attributeValueList = handleAttributeList(productParam.getProductAttributeValueList(), productId);
                // 批量保存属性值
                for (PmsProductAttributeValues attributeValue : attributeValueList) {
                    count += productAttributeValuesMapper.insert(attributeValue);
                }
            } catch (Exception e) {
                log.error("保存商品属性值失败", e);
                // 属性保存失败不影响商品创建，记录日志但不抛出异常
            }
        }

        log.info("商品创建完成，总计影响行数: {}", count);
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
        List<PmsProductSkus> result = new ArrayList<>();
        
        for (ProductParam.SkuStockParam skuParam : skuParamList) {
            PmsProductSkus sku = new PmsProductSkus();
            
            // 复制属性前检查skuParam是否为null
            if (skuParam == null) {
                log.warn("SKU参数为null，创建默认SKU");
                // 创建默认SKU
                sku.setProductId(productId);
                sku.setSkuCode(generateSkuCode(productId));
                sku.setPrice(new BigDecimal("0.01"));
                sku.setStock(100);
                sku.setLowStock(10);
                sku.setSale(0);
                sku.setLockStock(0);
                sku.setSpecs("{}");
                sku.setCreateTime(now);
                sku.setUpdateTime(now);
                result.add(sku);
                continue;
            }
            
            // 正常复制属性
            BeanUtils.copyProperties(skuParam, sku);
            
            // 设置商品ID
            sku.setProductId(productId);
            
            // 处理SKU编码
            if (StringUtils.isBlank(sku.getSkuCode())) {
                sku.setSkuCode(generateSkuCode(productId));
            }
            
            // 图片字段映射
            if (skuParam.getPic() != null && StringUtils.isNotBlank(skuParam.getPic())) {
                sku.setImage(skuParam.getPic());
            }
            
            // 规格数据处理
            if (skuParam.getSpecs() != null) {
                sku.setSpecs(skuParam.getSpecs());
            } else {
                sku.setSpecs("{}"); // 默认空规格
            }
            
            // 设置预警库存
            if (sku.getLowStock() == null) {
                sku.setLowStock(10);
            }
            
            // 设置销量
            if (sku.getSale() == null) {
                sku.setSale(0);
            }
            
            // 设置锁定库存
            if (sku.getLockStock() == null) {
                sku.setLockStock(0);
            }
            
            // 设置价格
            if (sku.getPrice() == null) {
                sku.setPrice(new BigDecimal("0.01"));
            }
            
            // 设置库存
            if (sku.getStock() == null) {
                sku.setStock(100);
            }
            
            // 设置时间
            sku.setCreateTime(now);
            sku.setUpdateTime(now);
            
            result.add(sku);
        }
        
        // 如果结果为空，创建默认SKU
        if (result.isEmpty()) {
            log.warn("SKU处理结果为空，创建默认SKU");
            PmsProductSkus defaultSku = new PmsProductSkus();
            defaultSku.setProductId(productId);
            defaultSku.setSkuCode(generateSkuCode(productId));
            defaultSku.setPrice(new BigDecimal("0.01"));
            defaultSku.setStock(100);
            defaultSku.setLowStock(10);
            defaultSku.setSale(0);
            defaultSku.setLockStock(0);
            defaultSku.setSpecs("{}");
            defaultSku.setCreateTime(now);
            defaultSku.setUpdateTime(now);
            result.add(defaultSku);
        }
        
        return result;
    }

    /**
     * 处理商品属性列表
     */
    private List<PmsProductAttributeValues> handleAttributeList(List<ProductParam.ProductAttributeValueParam> attributeParamList, Long productId) {
        List<PmsProductAttributeValues> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (ProductParam.ProductAttributeValueParam attributeParam : attributeParamList) {
            try {
                // 检查参数是否为null
                if (attributeParam == null) {
                    log.warn("属性参数为null，跳过");
                    continue;
                }
                
                PmsProductAttributeValues attributeValue = new PmsProductAttributeValues();
                attributeValue.setProductId(productId);
                
                // 设置属性ID，如果为null则使用默认值0
                if (attributeParam.getProductAttributeId() == null) {
                    log.warn("属性ID为null，使用默认值0");
                    attributeValue.setAttributeId(0L);
                } else {
                    attributeValue.setAttributeId(attributeParam.getProductAttributeId());
                }
                
                // 设置属性值，如果为null则使用空字符串
                if (attributeParam.getValue() == null) {
                    log.warn("属性值为null，使用空字符串");
                    attributeValue.setValue("");
                } else {
                    attributeValue.setValue(attributeParam.getValue());
                }
                
                attributeValue.setCreateTime(now);
                result.add(attributeValue);
            } catch (Exception e) {
                log.error("处理商品属性时出错: {}", attributeParam, e);
                // 继续处理下一个属性，不影响整体流程
            }
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

    @Override
    public List<PmsProductSkus> getProductSkus(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<PmsProductSkus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsProductSkus::getProductId, id);
        List<PmsProductSkus> list = productSkusMapper.selectList(wrapper);
        return list;
    }
}
