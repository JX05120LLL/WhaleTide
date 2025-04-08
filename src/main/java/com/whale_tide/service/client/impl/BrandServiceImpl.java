package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.brand.BrandNotFoundException;
import com.whale_tide.common.exception.product.ProductNotFoundException;
import com.whale_tide.dto.client.product.*;
import com.whale_tide.entity.pms.PmsBrands;
import com.whale_tide.entity.pms.PmsProductComments;
import com.whale_tide.entity.pms.PmsProductSkus;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.mapper.pms.PmsBrandsMapper;
import com.whale_tide.mapper.pms.PmsProductCommentsMapper;
import com.whale_tide.mapper.pms.PmsProductSkusMapper;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.service.client.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("clientBrandService")
public class BrandServiceImpl implements IBrandService {


    @Autowired
    private PmsProductsMapper pmsProductsMapper;
    @Autowired
    private PmsProductSkusMapper pmsProductSkusMapper;
    @Autowired
    private PmsBrandsMapper brandsMapper;

    @Autowired
    private PmsBrandsMapper pmsBrandsMapper;

    @Autowired
    private PmsProductCommentsMapper commentsMapper;

    /**
     * 获取品牌详情
     * @param id
     * @return
     */
    @Override
    public BrandDetailResponse getBrandDetail(Long id) {
        // 直接查询品牌信息
        PmsBrands brand = brandsMapper.selectById(id);
        if (brand == null) {
            throw new BrandNotFoundException("品牌不存在");
        }

        // 查询该品牌的商品数量
        LambdaQueryWrapper<PmsProducts> productQuery = new LambdaQueryWrapper<>();
        productQuery.eq(PmsProducts::getBrandId, id);
        Long productCount = pmsProductsMapper.selectCount(productQuery);

        // 查询该品牌的评论数量
        LambdaQueryWrapper<PmsProductComments> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.inSql(PmsProductComments::getProductId, 
            "SELECT id FROM pms_products WHERE brand_id = " + id);
        Long commentCount = commentsMapper.selectCount(commentQuery);

        // 封装品牌详情
        BrandDetailResponse response = new BrandDetailResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setLogo(brand.getLogo());
        response.setBigPic(brand.getLogo()); // 如果没有大图，使用logo
        response.setProductCount(productCount.intValue());
        response.setProductCommentCount(commentCount.intValue());
        response.setDescription(brand.getDescription());

        return response;
    }



    /**
     * 获取推荐品牌
     * @param request
     * @return
     */
    @Override
    public PageResponse<BrandListItemResponse> getRecommendBrands(RecommendBrandRequest request) {
        log.info("获取品牌列表, request={}", request);
        
        // 解析参数
        int pageNum = request.getPageNum().intValue();  // 当前页
        int pageSize = request.getPageSize().intValue(); // 每页记录数
        
        // 构建查询 - 直接查询品牌表
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsBrands::getStatus, 1); // 状态为1的品牌(显示)
        
        // 计算总记录数
        long total = pmsBrandsMapper.selectCount(queryWrapper);
        
        // 分页查询
        Page<PmsBrands> page = new Page<>(pageNum, pageSize);
        page = pmsBrandsMapper.selectPage(page, queryWrapper);
        
        // 获取品牌列表
        List<PmsBrands> brandList = page.getRecords();
        
        if (brandList.isEmpty()) {
            log.warn("未找到符合条件的品牌");
            return PageResponse.of(new ArrayList<>(), pageNum, pageSize, 0, 0);
        }
        
        // 计算每个品牌的产品数量
        List<Long> brandIds = brandList.stream()
            .map(PmsBrands::getId)
            .collect(Collectors.toList());
        
        // 查询各品牌的产品数量 
        Map<Long, Integer> productCountMap = new HashMap<>();
        if (!brandIds.isEmpty()) {
            // 查询每个品牌关联的产品数量
            LambdaQueryWrapper<PmsProducts> productQuery = new LambdaQueryWrapper<>();
            productQuery.in(PmsProducts::getBrandId, brandIds);
            List<PmsProducts> products = pmsProductsMapper.selectList(productQuery);
            
            productCountMap = products.stream()
                .collect(Collectors.groupingBy(
                    PmsProducts::getBrandId,
                    Collectors.summingInt(product -> 1) // 每个产品计数1
                ));
        }
        
        // 转换为响应对象
        List<BrandListItemResponse> responseList = new ArrayList<>();
        for (PmsBrands brand : brandList) {
            BrandListItemResponse item = new BrandListItemResponse();
            item.setId(brand.getId());
            item.setName(brand.getName());
            item.setLogo(brand.getLogo());
            // 设置产品数量，如果没有则为0
            item.setProductCount(productCountMap.getOrDefault(brand.getId(), 0));
            responseList.add(item);
        }
        
        log.info("品牌列表获取成功, 总记录数:{}, 总页数:{}", total, (total + pageSize - 1) / pageSize);
        int totalPages = (int)Math.ceil((double)total / pageSize);
        return PageResponse.of(responseList, pageNum, pageSize, total, totalPages);
    }



    /**
     * 获取品牌商品列表
     * @param request
     * @return ProductListItemResponse
     */
    @Override
    public PageResponse<ProductListItemResponse> getBrandProducts(BrandProductRequest request) {
        // 解析参数
        Long brandId = request.getBrandId();
        int pageNum = request.getPageNum().intValue();  // 转换为int类型
        int pageSize = request.getPageSize().intValue(); // 转换为int类型

        // 查询总记录数
        LambdaQueryWrapper<PmsProducts> countQueryWrapper = new LambdaQueryWrapper<>();
        countQueryWrapper.eq(PmsProducts::getBrandId, brandId);
        long total = pmsProductsMapper.selectCount(countQueryWrapper);

        // 计算总页数
        int totalPage = (int) Math.ceil((double) total / pageSize);

        // 分页查询数据
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getBrandId, brandId);
        queryWrapper.last("LIMIT " + (pageNum - 1) * pageSize + ", " + pageSize);
        List<PmsProducts> productList = pmsProductsMapper.selectList(queryWrapper);

        // 封装分页结果
        List<ProductListItemResponse> responseList = productList.stream()
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

        return PageResponse.of(responseList, pageNum, pageSize, total, totalPage);
    }
}

