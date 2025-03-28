package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.home.HomeCategoryResponse;
import com.whale_tide.dto.client.home.HomeContentResponse;
import com.whale_tide.dto.client.home.HomeProductRequest;
import com.whale_tide.dto.client.product.ProductListItemResponse;
import com.whale_tide.entity.pms.PmsBrands;
import com.whale_tide.entity.pms.PmsProductCategories;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.mapper.pms.PmsBrandsMapper;
import com.whale_tide.mapper.pms.PmsProductCategoriesMapper;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.service.client.IHomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页内容Service实现类
 */
@Service
@Slf4j
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private PmsProductsMapper productsMapper;
    
    @Autowired
    private PmsBrandsMapper brandsMapper;
    
    @Autowired
    private PmsProductCategoriesMapper categoryMapper;

    // 获取首页内容
    @Override
    public HomeContentResponse getHomeContent() {
        log.info("获取首页内容");
        HomeContentResponse result = new HomeContentResponse();
        
        // 获取轮播广告 - 直接使用热门商品当做轮播广告
        result.setAdvertiseList(getHomeAdvertiseList());
        
        // 获取推荐品牌
        result.setBrandList(getHomeBrandList());
        
        // 获取新品推荐
        result.setNewProductList(getHomeNewProductList());
        
        // 获取人气推荐
        result.setHotProductList(getHomeHotProductList());
        
        // 获取专题推荐
        result.setSubjectList(getHomeSubjectList());
        
        log.info("首页内容获取成功");
        return result;
    }

    // 获取首页推荐商品列表
    @Override
    public PageResponse<ProductListItemResponse> getRecommendProductList(HomeProductRequest request) {
        log.info("获取首页推荐商品列表, request={}", request);
        // 获取推荐商品列表
        int pageNum = request.pageNum != null ? request.pageNum.intValue() : 1;
        int pageSize = request.pageSize != null ? request.pageSize.intValue() : 10;
        
        // 构建查询条件
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getRecommendStatus, 1)  // 推荐状态为1
                   .eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                   .orderByDesc(PmsProducts::getSort);       // 按排序字段降序
        
        // 执行分页查询
        Page<PmsProducts> productPage = new Page<>(pageNum, pageSize);
        productsMapper.selectPage(productPage, queryWrapper);
        
        // 转换结果
        List<ProductListItemResponse> productList = productPage.getRecords().stream()
                .map(this::convertToProductListItemResponse)
                .collect(Collectors.toList());
        
        // 构建分页响应
        PageResponse<ProductListItemResponse> response = PageResponse.of(
                productList, 
                pageNum, 
                pageSize, 
                productPage.getTotal(), 
                (int) productPage.getPages()
        );
        
        log.info("首页推荐商品列表获取成功, 总记录数:{}, 总页数:{}", response.getTotal(), response.getTotalPage());
        return response;
    }

    // 获取首页新品列表
    @Override
    public PageResponse<ProductListItemResponse> getNewProductList(HomeProductRequest request) {
        log.info("获取首页新品列表, request={}", request);
        // 获取新品列表
        int pageNum = request.pageNum != null ? request.pageNum.intValue() : 1;
        int pageSize = request.pageSize != null ? request.pageSize.intValue() : 10;
        
        // 构建查询条件
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getNewStatus, 1)        // 新品状态为1
                   .eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                   .orderByDesc(PmsProducts::getCreateTime); // 按创建时间降序
        
        // 执行分页查询
        Page<PmsProducts> productPage = new Page<>(pageNum, pageSize);
        productsMapper.selectPage(productPage, queryWrapper);
        
        // 转换结果
        List<ProductListItemResponse> productList = productPage.getRecords().stream()
                .map(this::convertToProductListItemResponse)
                .collect(Collectors.toList());
        
        // 构建分页响应
        PageResponse<ProductListItemResponse> response = PageResponse.of(
                productList, 
                pageNum, 
                pageSize, 
                productPage.getTotal(), 
                (int) productPage.getPages()
        );
        
        log.info("首页新品列表获取成功, 总记录数:{}, 总页数:{}", response.getTotal(), response.getTotalPage());
        return response;
    }

    // 获取首页热卖商品列表
    @Override
    public PageResponse<ProductListItemResponse> getHotProductList(HomeProductRequest request) {
        log.info("获取首页热卖商品列表, request={}", request);
        // 获取热卖商品列表
        int pageNum = request.pageNum != null ? request.pageNum.intValue() : 1;
        int pageSize = request.pageSize != null ? request.pageSize.intValue() : 10;
        
        // 构建查询条件 - 按销量排序的热门商品
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                   .orderByDesc(PmsProducts::getSale);        // 按销量降序
        
        // 执行分页查询
        Page<PmsProducts> productPage = new Page<>(pageNum, pageSize);
        productsMapper.selectPage(productPage, queryWrapper);
        
        // 转换结果
        List<ProductListItemResponse> productList = productPage.getRecords().stream()
                .map(this::convertToProductListItemResponse)
                .collect(Collectors.toList());
        
        // 构建分页响应
        PageResponse<ProductListItemResponse> response = PageResponse.of(
                productList, 
                pageNum, 
                pageSize, 
                productPage.getTotal(), 
                (int) productPage.getPages()
        );
        
        log.info("首页热卖商品列表获取成功, 总记录数:{}, 总页数:{}", response.getTotal(), response.getTotalPage());
        return response;
    }
    
    /**
     * 获取首页轮播广告
     */
    private List<HomeContentResponse.HomeAdvertiseResponse> getHomeAdvertiseList() {
        log.info("获取首页轮播广告");
        
        // 从商品表中获取热门商品作为广告
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                   .orderByDesc(PmsProducts::getSale)         // 按销量降序
                   .last("LIMIT 4");                          // 只取4条记录
        
        List<PmsProducts> productList = productsMapper.selectList(queryWrapper);
        
        // 转换为广告对象
        List<HomeContentResponse.HomeAdvertiseResponse> advertiseList = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            advertiseList = productList.stream().map(item -> {
                HomeContentResponse.HomeAdvertiseResponse response = new HomeContentResponse.HomeAdvertiseResponse();
                response.setId(item.getId());
                response.setName(item.getName());
                response.setPic(item.getMainImage());
                response.setUrl("/product/detail/" + item.getId());
                return response;
            }).collect(Collectors.toList());
        }
        
        log.info("首页轮播广告获取成功, 数量:{}", advertiseList.size());
        return advertiseList;
    }
    
    /**
     * 获取首页推荐品牌
     */
    private List<HomeContentResponse.HomeBrandResponse> getHomeBrandList() {
        log.info("获取首页推荐品牌");
        
        // 构建查询条件：推荐品牌且状态为显示
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsBrands::getIsFeatured, 1)  // 推荐状态为1
                   .eq(PmsBrands::getStatus, 1)       // 显示状态为1
                   .orderByDesc(PmsBrands::getSort);  // 按排序字段降序排序
        
        List<PmsBrands> brandList = brandsMapper.selectList(queryWrapper);
        
        // 如果数据库中没有符合条件的品牌，返回空列表
        if (brandList == null || brandList.isEmpty()) {
            log.warn("未找到符合条件的推荐品牌，将获取任意品牌");
            
            // 不限制条件，获取任意品牌
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PmsBrands::getStatus, 1)       // 显示状态为1
                       .orderByDesc(PmsBrands::getSort)    // 按排序字段降序排序
                       .last("LIMIT 4");                  // 只取4条记录
            
            brandList = brandsMapper.selectList(queryWrapper);
        }
        
        // 转换为前端DTO
        List<HomeContentResponse.HomeBrandResponse> result = new ArrayList<>();
        if (brandList != null && !brandList.isEmpty()) {
            result = brandList.stream().map(item -> {
                HomeContentResponse.HomeBrandResponse response = new HomeContentResponse.HomeBrandResponse();
                response.setId(item.getId());
                response.setName(item.getName());
                response.setLogo(item.getLogo());
                return response;
            }).collect(Collectors.toList());
        }
        
        log.info("首页推荐品牌获取成功, 数量:{}", result.size());
        return result;
    }
    
    /**
     * 获取首页新品推荐
     */
    private List<ProductListItemResponse> getHomeNewProductList() {
        log.info("获取首页新品推荐");
        
        // 构建查询条件：新品且上架状态为1
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getNewStatus, 1)        // 新品状态为1
                   .eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                   .orderByDesc(PmsProducts::getCreateTime)  // 按创建时间降序
                   .last("LIMIT 4");                         // 只取4条记录
        
        List<PmsProducts> productList = productsMapper.selectList(queryWrapper);
        
        // 如果没有新品，则获取任意上架的产品
        if (productList == null || productList.isEmpty()) {
            log.warn("未找到符合条件的新品，将获取最新上架的商品");
            
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                       .orderByDesc(PmsProducts::getCreateTime)   // 按创建时间降序
                       .last("LIMIT 4");                          // 只取4条记录
            
            productList = productsMapper.selectList(queryWrapper);
        }
        
        // 转换为前端DTO
        List<ProductListItemResponse> result = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            result = productList.stream()
                    .map(this::convertToProductListItemResponse)
                    .collect(Collectors.toList());
        }
        
        log.info("首页新品推荐获取成功, 数量:{}", result.size());
        return result;
    }
    
    /**
     * 获取首页热卖商品推荐
     */
    private List<ProductListItemResponse> getHomeHotProductList() {
        log.info("获取首页热卖商品推荐");
        
        // 构建查询条件：按销量排序，且上架状态为1
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                   .orderByDesc(PmsProducts::getSale)         // 按销量降序
                   .last("LIMIT 4");                          // 只取4条记录
        
        List<PmsProducts> productList = productsMapper.selectList(queryWrapper);
        
        // 转换为前端DTO
        List<ProductListItemResponse> result = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            result = productList.stream()
                    .map(this::convertToProductListItemResponse)
                    .collect(Collectors.toList());
        }
        
        log.info("首页热卖商品推荐获取成功, 数量:{}", result.size());
        return result;
    }
    
    /**
     * 获取首页专题推荐
     */
    private List<HomeContentResponse.HomeSubjectResponse> getHomeSubjectList() {
        log.info("获取首页专题推荐");
        
        // 不使用cms_subject表，直接从商品表获取数据
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)     // 上架状态为1
                   .orderByDesc(PmsProducts::getCreateTime)   // 按创建时间降序
                   .last("LIMIT 4");                          // 只取4条记录
        
        List<PmsProducts> productList = productsMapper.selectList(queryWrapper);
        
        // 转换为前端DTO
        List<HomeContentResponse.HomeSubjectResponse> result = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            result = productList.stream().map(item -> {
                HomeContentResponse.HomeSubjectResponse response = new HomeContentResponse.HomeSubjectResponse();
                response.setId(item.getId());
                response.setTitle(item.getName());
                response.setPic(item.getMainImage());
                response.setCategoryId(item.getCategoryId());
                response.setDescription(item.getBrief() != null ? item.getBrief() : "产品描述");
                return response;
            }).collect(Collectors.toList());
        }
        
        log.info("首页专题推荐获取成功(从商品表), 数量:{}", result.size());
        return result;
    }
    
    /**
     * 将商品实体转换为响应DTO
     */
    private ProductListItemResponse convertToProductListItemResponse(PmsProducts product) {
        ProductListItemResponse response = new ProductListItemResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPic(product.getMainImage());
        response.setPrice(product.getPrice());
        response.setSale(product.getSale());
        return response;
    }

    /**
     * 获取商品分类列表
     * @param parentId 父分类ID
     * @return 分类列表
     */
    @Override
    public List<HomeCategoryResponse> getCategoryList(Long parentId) {
        log.info("获取商品分类列表, parentId={}", parentId);
        
        // 查询指定父分类下的所有分类
        LambdaQueryWrapper<PmsProductCategories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProductCategories::getParentId, parentId)
                   .eq(PmsProductCategories::getStatus, 1)      // 状态为启用的分类
                   .orderByAsc(PmsProductCategories::getSort);  // 按排序字段升序排序
        
        List<PmsProductCategories> categoryList = categoryMapper.selectList(queryWrapper);
        
        // 如果没有找到分类，返回空列表
        if (categoryList == null || categoryList.isEmpty()) {
            log.warn("没有找到父分类ID为{}的商品分类", parentId);
            return new ArrayList<>();
        }
        
        // 转换为前端DTO
        List<HomeCategoryResponse> result = categoryList.stream().map(item -> {
            HomeCategoryResponse response = new HomeCategoryResponse();
            response.setId(item.getId());
            response.setName(item.getName());
            response.setIcon(item.getIcon());
            response.setLevel(item.getLevel());
            
            // 递归查询子分类
            response.setChildren(getCategoryList(item.getId()));
            
            return response;
        }).collect(Collectors.toList());
        
        log.info("商品分类列表获取成功, 数量:{}", result.size());
        return result;
    }
} 