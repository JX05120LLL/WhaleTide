package com.whaletide.client.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whaletide.client.product.dto.ProductSearchDTO;
import com.whaletide.client.product.service.IProductService;
import com.whaletide.client.product.vo.CategoryTreeVO;
import com.whaletide.client.product.vo.ProductDetailVO;
import com.whaletide.client.product.vo.ProductListItemVO;
import com.whaletide.client.product.vo.ProductSuggestionVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.api.CommonResult;
import com.whaletide.common.entity.pms.PmsBrands;
import com.whaletide.common.entity.pms.PmsProductCategories;
import com.whaletide.common.entity.pms.PmsProducts;
import com.whaletide.common.exception.base.ValidationException;
import com.whaletide.common.exception.product.ProductNotFoundException;
import com.whaletide.common.mapper.pms.PmsBrandsMapper;
import com.whaletide.common.mapper.pms.PmsProductCategoriesMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.whaletide.client.product.vo.BrandVO;

/**
 * 商品服务实现类
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final PmsProductsMapper productsMapper;
    private final PmsProductCategoriesMapper categoriesMapper;
    private final PmsBrandsMapper brandsMapper;

    @Autowired
    public ProductServiceImpl(RedisTemplate<String, Object> redisTemplate,
                             PmsProductsMapper productsMapper,
                             PmsProductCategoriesMapper categoriesMapper,
                             PmsBrandsMapper brandsMapper) {
        this.redisTemplate = redisTemplate;
        this.productsMapper = productsMapper;
        this.categoriesMapper = categoriesMapper;
        this.brandsMapper = brandsMapper;
    }

    // 关键词搜索商品
    @Override
    public List<ProductListItemVO> getProductsByKeyword(ProductSearchDTO searchDTO) {
        if (searchDTO == null) {
            throw new ValidationException("搜索参数不能为空");
        }
        
        log.info("搜索商品, 关键词: {}, 分类: {}, 页码: {}, 每页数量: {}",
                searchDTO.getKeyword(), searchDTO.getCategoryId(), searchDTO.getPageNum(), searchDTO.getPageSize());
        
        // 构建缓存Key
        String cacheKey = "product:search:" + searchDTO.getKeyword() + ":" + searchDTO.getCategoryId() + ":" 
            + searchDTO.getPageNum() + ":" + searchDTO.getPageSize();
        
        // 尝试从缓存获取
        List<ProductListItemVO> cacheResult = (List<ProductListItemVO>) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取商品搜索结果");
            return cacheResult;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (StringUtils.hasText(searchDTO.getKeyword())) {
            queryWrapper.like(PmsProducts::getName, searchDTO.getKeyword());
        }
        
        // 分类筛选
        if (searchDTO.getCategoryId() != null) {
            queryWrapper.eq(PmsProducts::getCategoryId, searchDTO.getCategoryId());
        }
        
        // 只查询上架商品
        queryWrapper.eq(PmsProducts::getPublishStatus, 1);
        
        // 分页查询
        Page<PmsProducts> page = new Page<>(searchDTO.getPageNum(), searchDTO.getPageSize());
        Page<PmsProducts> productPage = productsMapper.selectPage(page, queryWrapper);
        
        // 将结果列表转换为VO
        List<ProductListItemVO> productList = new ArrayList<>();
        if (productPage.getRecords() != null && !productPage.getRecords().isEmpty()) {
            for (PmsProducts product : productPage.getRecords()) {
                ProductListItemVO item = new ProductListItemVO();
                item.setId(product.getId());
                item.setName(product.getName());
                item.setPrice(product.getPrice());
                
                // 处理图片路径，确保存在且正确
                String imagePath = product.getMainImage();
                if (imagePath == null || imagePath.trim().isEmpty() || !imagePath.startsWith("/")) {
                    // 获取商品分类
                    Long categoryId = product.getCategoryId();
                    if (categoryId != null) {
                        // 查询分类
                        PmsProductCategories category = categoriesMapper.selectById(categoryId);
                        if (category != null) {
                            // 根据分类设置默认图片
                            Long catId = category.getId();
                            Long parentId = category.getParentId();
                            
                            if (catId == 1 || catId == 4 || catId == 5 || 
                                parentId == 1 || parentId == 4 || parentId == 5) { // 海洋设备类
                                imagePath = "/static/images/products/marine-binoculars.png";
                            } else if (catId == 2 || catId == 6 || catId == 7 || 
                                      parentId == 2 || parentId == 6 || parentId == 7) { // 潜水装备类
                                imagePath = "/static/images/products/diving-mask.png";
                            } else if (catId == 3 || catId == 8 || catId == 9 || 
                                      parentId == 3 || parentId == 8 || parentId == 9) { // 海滩用品类
                                imagePath = "/static/images/products/beach-cart.png";
                            } else {
                                imagePath = "/static/images/products/boat-fenders.png";
                            }
                        } else {
                            imagePath = "/static/images/products/boat-fenders.png";
                        }
                    } else {
                        imagePath = "/static/images/products/boat-fenders.png";
                    }
                }
                
                // 确保图片路径有效 - 如果仍然没有有效路径，使用备用图片
                if (imagePath == null || imagePath.trim().isEmpty()) {
                    imagePath = "/static/images/products/default-product.png";
                } else if (imagePath.startsWith("/") && !imagePath.startsWith("/static/")) {
                    // 修正图片路径，确保以/static开头
                    imagePath = "/static" + imagePath;
                }
                
                item.setPic(imagePath);
                item.setStock(product.getStock());
                
                // 设置销量
                if (product.getProductRating() != null) {
                    item.setSoldCount(product.getProductRating().intValue() * 100);
                } else {
                    item.setSoldCount(0);
                }
                
                productList.add(item);
            }
        }
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, productList, 10, TimeUnit.MINUTES);
        
        return productList;
    }

    // 获取商品分类树
    @Override
    public CategoryTreeVO getCategoryTree() {
        // 构建缓存Key
        String cacheKey = "product:category:tree";
        
        // 尝试从缓存获取
        CategoryTreeVO cacheResult = (CategoryTreeVO) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取商品分类树");
            return cacheResult;
        }
        
        // 查询所有分类
        List<PmsProductCategories> allCategories = categoriesMapper.selectList(null);
        if (allCategories == null || allCategories.isEmpty()) {
            return new CategoryTreeVO();
        }
        
        // 构建分类树
        CategoryTreeVO rootCategory = new CategoryTreeVO();
        rootCategory.setId(0L);
        rootCategory.setName("全部分类");
        
        // 查找一级分类
        List<CategoryTreeVO> firstLevelCategories = allCategories.stream()
            .filter(category -> category.getParentId() == 0)
            .map(category -> {
                CategoryTreeVO categoryVO = new CategoryTreeVO();
                categoryVO.setId(category.getId());
                categoryVO.setName(category.getName());
                return categoryVO;
            })
            .collect(Collectors.toList());
        
        // 为每个一级分类查找子分类
        firstLevelCategories.forEach(firstLevel -> {
            List<CategoryTreeVO> children = allCategories.stream()
                .filter(category -> Objects.equals(category.getParentId(), firstLevel.getId()))
                .map(category -> {
                    CategoryTreeVO categoryVO = new CategoryTreeVO();
                    categoryVO.setId(category.getId());
                    categoryVO.setName(category.getName());
                    return categoryVO;
                })
                .collect(Collectors.toList());
            
            firstLevel.setChildren(children);
        });
        
        rootCategory.setChildren(firstLevelCategories);
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, rootCategory, 1, TimeUnit.HOURS);
        
        return rootCategory;
    }

    // 获取商品详情
    @Override
    public ProductDetailVO getProductDetail(Long id) {
        if (id == null) {
            throw new ValidationException("商品ID不能为空");
        }
        
        log.info("获取商品详情, ID: {}", id);
        
        // 构建缓存Key
        String cacheKey = "product:detail:" + id;
        
        // 尝试从缓存获取
        ProductDetailVO cacheResult = (ProductDetailVO) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取商品详情");
            return cacheResult;
        }
        
        // 查询商品基本信息
        PmsProducts product = productsMapper.selectById(id);
        if (product == null) {
            throw new ProductNotFoundException("商品不存在: " + id);
        }
        
        // 查询品牌信息
        PmsBrands brand = null;
        if (product.getBrandId() != null) {
            brand = brandsMapper.selectById(product.getBrandId());
        }
        
        // 查询分类信息
        PmsProductCategories category = null;
        if (product.getCategoryId() != null) {
            category = categoriesMapper.selectById(product.getCategoryId());
        }
        
        // 构建商品详情VO
        ProductDetailVO detail = new ProductDetailVO();
        detail.setId(product.getId());
        detail.setName(product.getName());
        detail.setPrice(product.getPrice());
        detail.setStock(product.getStock());
        
        // 处理图片路径
        String mainImage = product.getMainImage();
        if (mainImage == null || mainImage.trim().isEmpty() || !mainImage.startsWith("/")) {
            // 根据分类设置默认图片
            if (category != null) {
                Long categoryId = category.getId();
                Long parentId = category.getParentId();
                
                if (categoryId == 1 || categoryId == 4 || categoryId == 5 || 
                    parentId == 1 || parentId == 4 || parentId == 5) { // 海洋设备类
                    mainImage = "/static/images/products/marine-binoculars.png";
                } else if (categoryId == 2 || categoryId == 6 || categoryId == 7 || 
                           parentId == 2 || parentId == 6 || parentId == 7) { // 潜水装备类
                    mainImage = "/static/images/products/diving-mask.png";
                } else if (categoryId == 3 || categoryId == 8 || categoryId == 9 || 
                           parentId == 3 || parentId == 8 || parentId == 9) { // 海滩用品类
                    mainImage = "/static/images/products/beach-cart.png";
                } else {
                    mainImage = "/static/images/products/boat-fenders.png";
                }
            } else {
                mainImage = "/static/images/products/boat-fenders.png";
            }
        }
        
        // 确保图片路径有效 - 如果仍然没有有效路径，使用备用图片
        if (mainImage == null || mainImage.trim().isEmpty()) {
            mainImage = "/static/images/products/default-product.png";
        } else if (mainImage.startsWith("/") && !mainImage.startsWith("/static/")) {
            // 修正图片路径，确保以/static开头
            mainImage = "/static" + mainImage;
        }
        
        detail.setPic(mainImage);

        
        // 处理商品图片
        List<String> albumPics = new ArrayList<>();
        // 添加主图
        if (StringUtils.hasText(mainImage)) {
            albumPics.add(mainImage);
        }

        // 处理商品副图 - 修复显示其他商品图片的问题
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            log.info("商品 {} 获取到副图数据: {}", id, product.getProductImages());
            
            // 遍历商品副图，只添加当前商品的相关图片
            for (String imgPath : product.getProductImages()) {
                // 只添加有效的图片路径
                if (imgPath != null && !imgPath.isEmpty()) {
                    // 修正图片路径，确保以/static开头
                    if (imgPath.startsWith("/") && !imgPath.startsWith("/static/")) {
                        imgPath = "/static" + imgPath;
                    }
                    // 添加图片之前打印日志，帮助诊断
                    log.debug("添加商品副图: {}", imgPath);
                    albumPics.add(imgPath);
                }
            }
            
            // 副图验证 - 如果添加副图后，数组长度仍然为1(只有主图)，则说明可能副图有问题
            if (albumPics.size() <= 1) {
                log.warn("商品 {} 副图可能有问题，长度为: {}", id, albumPics.size());
                // 添加默认副图
                albumPics.add("/static/images/products/default-product-1.png");
                albumPics.add("/static/images/products/default-product-2.png");
            }
        } else {
            log.info("商品 {} 没有副图数据", id);
            // 添加默认副图
            albumPics.add("/static/images/products/default-product-1.png");
            albumPics.add("/static/images/products/default-product-2.png");
        }

        detail.setAlbumPics(albumPics);
        
        // 商品详情描述
        detail.setDescription(product.getBrief());
        
        // 设置分类信息
        if (category != null) {
            detail.setCategoryId(category.getId());
            detail.setCategoryName(category.getName());
        }
        
        // 设置品牌信息
        if (brand != null) {
            detail.setBrandId(brand.getId());
            detail.setBrandName(brand.getName());
        }
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, detail, 1, TimeUnit.HOURS);
        
        return detail;
    }

    @Override
    public List<String> getHotKeywords() {
        // 构建缓存Key
        String cacheKey = "product:hotKeywords";
        
        // 尝试从缓存获取
        List<String> cacheResult = (List<String>) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取热门搜索关键词");
            return cacheResult;
        }
        
        // 查询热门商品，按照创建时间降序排序
        List<PmsProducts> hotProducts = productsMapper.selectList(
            new LambdaQueryWrapper<PmsProducts>()
                .orderByDesc(PmsProducts::getCreateTime)
                .last("LIMIT 10")
        );
        
        // 热门关键词列表
        List<String> hotKeywords = new ArrayList<>();
        
        // 如果没有足够的关键字，添加一些默认关键字
        List<String> defaultKeywords = Arrays.asList("手机", "电脑", "相机", "耳机", "平板", "手表");
        for (String keyword : defaultKeywords) {
            if (hotKeywords.size() < 6 && !hotKeywords.contains(keyword)) {
                hotKeywords.add(keyword);
            }
        }
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, hotKeywords, 1, TimeUnit.HOURS);
        
        return hotKeywords;
    }

    @Override
    public List<ProductSuggestionVO> getSuggestions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }
        
        log.info("获取搜索建议, 关键词: {}", keyword);
        
        // 构建缓存Key
        String cacheKey = "product:suggestion:" + keyword;
        
        // 尝试从缓存获取
        List<ProductSuggestionVO> cacheResult = (List<ProductSuggestionVO>) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取搜索建议");
            return cacheResult;
        }
        
        // 查询匹配的商品
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(PmsProducts::getName, keyword)
            .or().like(PmsProducts::getProductDetails, keyword)
            .eq(PmsProducts::getPublishStatus, 1)
            .orderByDesc(PmsProducts::getCreateTime)
            .last("LIMIT 10");
        
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        // 转换为建议VO
        List<ProductSuggestionVO> suggestions = new ArrayList<>();
        if (products != null && !products.isEmpty()) {
            suggestions = products.stream().map(product -> {
                ProductSuggestionVO suggestion = new ProductSuggestionVO();
                suggestion.setProductId(product.getId());
                suggestion.setProductName(product.getName());
                suggestion.setPic(product.getMainImage());
                return suggestion;
            }).collect(Collectors.toList());
        }
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, suggestions, 10, TimeUnit.MINUTES);
        
        return suggestions;
    }

    @Override
    public List<BrandVO> getBrands(Integer limit) {
        log.info("获取品牌列表, 限制数量: {}", limit);
        
        // 构建缓存Key
        String cacheKey = "product:brands:limit:" + limit;
        
        // 尝试从缓存获取
        List<BrandVO> cacheResult = (List<BrandVO>) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取品牌列表");
            return cacheResult;
        }
        
        // 构建查询条件，只查询显示状态的品牌
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsBrands::getStatus, 1)
                .orderByAsc(PmsBrands::getCreateTime);
        
        // 限制返回数量
        if (limit != null && limit > 0) {
            queryWrapper.last("LIMIT " + limit);
        }
        
        // 执行查询
        List<PmsBrands> brands = brandsMapper.selectList(queryWrapper);
        
        // 转换为VO对象
        List<BrandVO> brandVOList = brands.stream().map(brand -> {
            BrandVO vo = new BrandVO();
            vo.setId(brand.getId());
            vo.setName(brand.getName());
            vo.setLogo(brand.getLogo());
            vo.setDescription(brand.getDescription());
            // 设置默认值或从PmsBrands中获取已有属性
            vo.setFirstLetter("");  // 设置默认值
            vo.setSort(0);          // 设置默认值
            vo.setFactoryStatus(0); // 设置默认值
            vo.setShowStatus(brand.getStatus());
            vo.setProductCount(0);  // 设置默认值
            vo.setProductCommentCount(0); // 设置默认值
            return vo;
        }).collect(Collectors.toList());
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, brandVOList, 10, TimeUnit.MINUTES);
        
        return brandVOList;
    }
    
    /**
     * 获取分类商品列表
     */
    @Override
    public List<ProductListItemVO> getCategoryProducts(Long categoryId, Integer page, Integer pageSize, String sort) {
        if (categoryId == null) {
            throw new ValidationException("分类ID不能为空");
        }
        
        log.info("获取分类商品列表, 分类ID: {}, 页码: {}, 每页数量: {}, 排序: {}", 
                categoryId, page, pageSize, sort);
        
        // 构建缓存Key
        String cacheKey = "product:category:" + categoryId + ":" + page + ":" + pageSize + ":" + sort;
        
        // 尝试从缓存获取
        List<ProductListItemVO> cacheResult = (List<ProductListItemVO>) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取分类商品列表");
            return cacheResult;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        
        // 分类筛选 - 查询当前分类及其子分类下的商品
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);
        
        // 查询子分类IDs
        List<PmsProductCategories> childCategories = categoriesMapper.selectList(
                new LambdaQueryWrapper<PmsProductCategories>()
                        .eq(PmsProductCategories::getParentId, categoryId));
        
        if (childCategories != null && !childCategories.isEmpty()) {
            List<Long> childIds = childCategories.stream()
                    .map(PmsProductCategories::getId)
                    .collect(Collectors.toList());
            categoryIds.addAll(childIds);
        }
        
        queryWrapper.in(PmsProducts::getCategoryId, categoryIds);
        
        // 只查询上架商品
        queryWrapper.eq(PmsProducts::getPublishStatus, 1);
        
        // 排序处理
        if (StringUtils.hasText(sort)) {
            switch (sort) {
                case "price_asc":
                    queryWrapper.orderByAsc(PmsProducts::getPrice);
                    break;
                case "price_desc":
                    queryWrapper.orderByDesc(PmsProducts::getPrice);
                    break;
                case "sales_desc":
                    // 商品没有销量字段，可以改用其他字段或保持默认排序
                    queryWrapper.orderByDesc(PmsProducts::getProductRating);
                    break;
                case "create_time_desc":
                    queryWrapper.orderByDesc(PmsProducts::getCreateTime);
                    break;
                default:
                    // 商品没有排序字段，使用推荐状态和创建时间
                    queryWrapper.orderByDesc(PmsProducts::getRecommendStatus);
                    break;
            }
        } else {
            // 默认按推荐排序
            queryWrapper.orderByDesc(PmsProducts::getRecommendStatus);
        }
        
        // 分页查询
        Page<PmsProducts> productPage = new Page<>(page, pageSize);
        Page<PmsProducts> resultPage = productsMapper.selectPage(productPage, queryWrapper);
        
        // 转换为VO列表
        List<ProductListItemVO> productList = resultPage.getRecords().stream()
                .map(product -> {
                    ProductListItemVO item = new ProductListItemVO();
                    item.setId(product.getId());
                    item.setName(product.getName());
                    item.setPrice(product.getPrice());
                    
                    // 处理图片路径，确保存在且正确
                    String imagePath = product.getMainImage();
                    // 如果数据库中的图片路径为空或不存在，使用默认图片
                    if (imagePath == null || imagePath.trim().isEmpty() || !imagePath.startsWith("/")) {
                        // 根据分类设置默认图片
                        if (categoryId == 1 || categoryIds.contains(4L) || categoryIds.contains(5L)) { // 海洋设备类
                            imagePath = "/static/images/products/marine-binoculars.png";  // 使用已有图片
                        } else if (categoryId == 2 || categoryIds.contains(6L) || categoryIds.contains(7L)) { // 潜水装备类
                            imagePath = "/static/images/products/diving-mask.png";  // 使用已有图片
                        } else if (categoryId == 3 || categoryIds.contains(8L) || categoryIds.contains(9L)) { // 海滩用品类
                            imagePath = "/static/images/products/beach-cart.png";  // 使用已有图片
                        } else {
                            imagePath = "/static/images/products/boat-fenders.png";  // 使用已有图片作为通用默认图
                        }
                    }
                    
                    // 确保图片路径有效 - 如果仍然没有有效路径，使用备用图片
                    if (imagePath == null || imagePath.trim().isEmpty()) {
                        imagePath = "/static/images/products/default-product.png";
                    } else if (imagePath.startsWith("/") && !imagePath.startsWith("/static/")) {
                        // 修正图片路径，确保以/static开头
                        imagePath = "/static" + imagePath;
                    }
                    
                    item.setPic(imagePath);
                    
                    // 使用评分作为模拟销量
                    if (product.getProductRating() != null) {
                        item.setSoldCount(product.getProductRating().intValue() * 100);
                    } else {
                        item.setSoldCount(0);
                    }
                    return item;
                })
                .collect(Collectors.toList());
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, productList, 10, TimeUnit.MINUTES);
        
        return productList;
    }

    /**
     * 获取相关商品推荐
     */
    @Override
    public List<ProductListItemVO> getRelatedProducts(Long productId, Integer limit) {
        // 构建缓存Key
        String cacheKey = "product:related:" + productId + ":" + limit;
        
        // 尝试从缓存获取
        List<ProductListItemVO> cacheResult = (List<ProductListItemVO>) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取相关商品推荐");
            return cacheResult;
        }
        
        // 获取当前商品信息
        PmsProducts currentProduct = productsMapper.selectById(productId);
        if (currentProduct == null) {
            return new ArrayList<>();
        }
        
        // 查询同类别商品
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getCategoryId, currentProduct.getCategoryId())
                    .ne(PmsProducts::getId, productId)  // 排除当前商品
                    .eq(PmsProducts::getPublishStatus, 1)  // 只查询上架商品
                    .orderByDesc(PmsProducts::getProductRating)  // 按评分降序(替代销量)
                    .last("LIMIT " + limit);  // 限制结果数量
        
        List<PmsProducts> relatedProducts = productsMapper.selectList(queryWrapper);
        
        // 转换为VO列表
        List<ProductListItemVO> result = relatedProducts.stream().map(product -> {
            ProductListItemVO item = new ProductListItemVO();
            item.setId(product.getId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setPic(product.getMainImage());
            item.setStock(product.getStock());
            
            // 设置销量
            if (product.getProductRating() != null) {
                item.setSoldCount(product.getProductRating().intValue() * 100);
            } else {
                item.setSoldCount(0);
            }
            
            return item;
        }).collect(Collectors.toList());
        
        // 如果结果少于请求数量，可以添加其他类别的热门商品来补充
        if (result.size() < limit) {
            int remaining = limit - result.size();
            
            // 查询其他类别的热门商品
            LambdaQueryWrapper<PmsProducts> otherQueryWrapper = new LambdaQueryWrapper<>();
            otherQueryWrapper.ne(PmsProducts::getCategoryId, currentProduct.getCategoryId())
                            .ne(PmsProducts::getId, productId)
                            .eq(PmsProducts::getPublishStatus, 1)
                            .orderByDesc(PmsProducts::getProductRating)  // 按评分降序(替代销量)
                            .last("LIMIT " + remaining);
            
            List<PmsProducts> otherProducts = productsMapper.selectList(otherQueryWrapper);
            
            // 转换为VO列表并添加到结果中
            List<ProductListItemVO> otherItems = otherProducts.stream().map(product -> {
                ProductListItemVO item = new ProductListItemVO();
                item.setId(product.getId());
                item.setName(product.getName());
                item.setPrice(product.getPrice());
                item.setPic(product.getMainImage());
                item.setStock(product.getStock());
                
                // 设置销量
                if (product.getProductRating() != null) {
                    item.setSoldCount(product.getProductRating().intValue() * 100);
                } else {
                    item.setSoldCount(0);
                }
                
                return item;
            }).collect(Collectors.toList());
            
            result.addAll(otherItems);
        }
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.MINUTES);
        
        return result;
    }
} 