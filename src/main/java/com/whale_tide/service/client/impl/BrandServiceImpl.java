package com.whale_tide.service.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // 根据id拿到商品
        PmsProducts product = pmsProductsMapper.selectById(id);
        if (product == null) throw new ProductNotFoundException("商品不存在");
        // 获得品牌id
        Long brandId = product.getBrandId();
        // 根据品牌id查询品牌信息
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsBrands::getId, brandId);
        PmsBrands brand = brandsMapper.selectOne(queryWrapper);

        if (brand == null) throw new BrandNotFoundException("品牌不存在");

        //查询商品数量
        Long l = pmsProductSkusMapper.selectCount(new LambdaQueryWrapper<PmsProductSkus>().eq(PmsProductSkus::getProductId, id));
        //查询评论数量
        Long c = commentsMapper.selectCount(new LambdaQueryWrapper<PmsProductComments>().eq(PmsProductComments::getProductId, id));
        // 封装品牌详情
        BrandDetailResponse response = new BrandDetailResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setLogo(brand.getLogo());
        response.setBigPic(product.getMainImage());
        response.setProductCount(l.intValue());
        response.setProductCommentCount(c.intValue());
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

        // 解析参数
        int pageNum = request.getPageNum().intValue();  // 当前页
        int pageSize = request.getPageSize().intValue(); // 每页记录数
        // 构建查询
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getRecommendStatus, 1);// 1 表示推荐状态
        queryWrapper.last("LIMIT " + (pageNum - 1) * pageSize + ", " + pageSize);
        // 查询数据
        List<PmsProducts> productList = pmsProductsMapper.selectList(queryWrapper);
        
        // 获取品牌ID (添加去重逻辑避免重复ID)
        List<Long> brandIdList = productList.stream()
            .map(PmsProducts::getBrandId)
            .distinct() // 确保ID列表中没有重复
            .collect(Collectors.toList());
            
        if (brandIdList.isEmpty()) {
            // 如果没有找到品牌ID，返回空结果
            return PageResponse.of(new ArrayList<>(), pageNum, pageSize, 0, 0);
        }
        
        // 查询品牌信息
        List<PmsBrands> brandList = pmsBrandsMapper.selectBatchIds(brandIdList);
        
        // 构建ID到品牌的映射，以便后续快速查找
        Map<Long, PmsBrands> brandMap = brandList.stream()
            .collect(Collectors.toMap(PmsBrands::getId, brand -> brand));
            
        // 构建ID到销量的映射
        Map<Long, Integer> productCountMap = productList.stream()
            .collect(Collectors.groupingBy(
                PmsProducts::getBrandId,
                Collectors.summingInt(product -> product.getSale() != null ? product.getSale() : 0)
            ));

        //封装结果
        List<BrandListItemResponse> responseList = new ArrayList<>();
        for (Long brandId : brandIdList) {
            PmsBrands brand = brandMap.get(brandId);
            if (brand != null) {
                BrandListItemResponse response = new BrandListItemResponse();
                response.setId(brandId); // 品牌ID
                response.setName(brand.getName()); // 品牌名称
                response.setLogo(brand.getLogo()); // 品牌logo
                response.setProductCount(productCountMap.getOrDefault(brandId, 0)); // 品牌商品数量
                responseList.add(response);
            }
        }

        // 分页处理
        int total = responseList.size();
        int totalPage = (int) Math.ceil((double) total / pageSize);
        return PageResponse.of(responseList, pageNum, pageSize, total, totalPage);
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

