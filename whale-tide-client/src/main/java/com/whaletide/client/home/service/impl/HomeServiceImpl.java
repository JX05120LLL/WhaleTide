package com.whaletide.client.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.client.home.service.IHomeService;
import com.whaletide.client.home.vo.BannerVO;
import com.whaletide.client.product.vo.ProductListItemVO;
import com.whaletide.common.entity.pms.PmsProducts;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页服务实现类
 */
@Slf4j
@Service
public class HomeServiceImpl implements IHomeService {

    private final PmsProductsMapper productsMapper;
    
    public HomeServiceImpl(PmsProductsMapper productsMapper) {
        this.productsMapper = productsMapper;
    }

    @Override
    public List<BannerVO> getBanners() {
        // 直接从商品表中查询推荐商品作为轮播图数据
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)  // 已上架
                .eq(PmsProducts::getRecommendStatus, 1)    // 推荐商品
                .orderByDesc(PmsProducts::getCreateTime)
                .last("LIMIT 5");  // 限制返回5条
        
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        // 转换为BannerVO对象
        return products.stream().map(product -> {
            BannerVO vo = new BannerVO();
            vo.setId(product.getId());
            vo.setTitle(product.getName());
            vo.setSubtitle(product.getBrief());
            vo.setImage(product.getMainImage());
            vo.setLink("/product/detail/" + product.getId());
            vo.setSort(0);  // 默认排序值
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductListItemVO> getHotProducts(Integer limit) {
        // 按销量降序查询热门商品，这里改用推荐状态和创建时间排序替代销量
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)  // 已上架
                .eq(PmsProducts::getRecommendStatus, 1)    // 推荐的商品
                .orderByDesc(PmsProducts::getCreateTime)
                .last("LIMIT " + limit);
        
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        // 转换为VO对象
        return convertToProductListItemVOs(products);
    }

    @Override
    public List<ProductListItemVO> getNewProducts(Integer limit) {
        // 按创建时间降序查询新品
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)  // 已上架
                .eq(PmsProducts::getNewStatus, 1)          // 新品状态
                .orderByDesc(PmsProducts::getCreateTime)
                .last("LIMIT " + limit);
        
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        // 转换为VO对象
        return convertToProductListItemVOs(products);
    }

    @Override
    public List<ProductListItemVO> getPromotionProducts(Integer limit) {
        // 查询促销商品，使用price字段代替promotionPrice
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1)  // 已上架
                .orderByAsc(PmsProducts::getPrice)         // 按价格排序
                .last("LIMIT " + limit);
        
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        // 转换为VO对象
        return convertToProductListItemVOs(products);
    }
    
    @Override
    public List<ProductListItemVO> getDiscountProducts(Integer limit) {
        // 查询限时折扣商品
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1);  // 已上架
        
        // 根据数据库结构，我们无法直接比较价格，将采用其他查询策略
        // 这里查询所有已上架的商品，按价格排序作为折扣商品
        queryWrapper.orderByAsc(PmsProducts::getPrice)
                   .last("LIMIT " + limit);
        
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        // 转换为VO对象
        return convertToProductListItemVOs(products);
    }
    
    @Override
    public List<ProductListItemVO> getSelectedProducts(Integer limit) {
        // 查询优选好物商品
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProducts::getPublishStatus, 1);   // 已上架
        
        // 由于没有PreferredStatus字段，可以使用recommendStatus字段替代
        queryWrapper.eq(PmsProducts::getRecommendStatus, 1)  // 推荐状态
                   .orderByDesc(PmsProducts::getCreateTime)  // 按创建时间排序
                   .last("LIMIT " + limit);
        
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        // 转换为VO对象
        return convertToProductListItemVOs(products);
    }
    
    /**
     * 将商品实体转换为VO对象
     */
    private List<ProductListItemVO> convertToProductListItemVOs(List<PmsProducts> products) {
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        
        return products.stream().map(product -> {
            ProductListItemVO vo = new ProductListItemVO();
            vo.setId(product.getId());
            vo.setName(product.getName());
            vo.setPrice(product.getPrice());
            vo.setPic(product.getMainImage());  // 使用mainImage代替pic
            vo.setStock(product.getStock());
            vo.setCategoryId(product.getCategoryId());
            vo.setBrandId(product.getBrandId());
            
            // 如果需要分类名称和品牌名称，需要进一步查询
            // 为简化处理这里未实现
            
            return vo;
        }).collect(Collectors.toList());
    }
} 