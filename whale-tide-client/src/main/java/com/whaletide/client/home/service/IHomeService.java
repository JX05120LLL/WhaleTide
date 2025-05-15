package com.whaletide.client.home.service;

import com.whaletide.client.home.vo.BannerVO;
import com.whaletide.client.product.vo.ProductListItemVO;

import java.util.List;

/**
 * 首页服务接口
 */
public interface IHomeService {

    /**
     * 获取首页轮播图
     * @return 轮播图列表
     */
    List<BannerVO> getBanners();

    /**
     * 获取热门商品
     * @param limit 数量限制
     * @return 热门商品列表
     */
    List<ProductListItemVO> getHotProducts(Integer limit);

    /**
     * 获取新品上市
     * @param limit 数量限制
     * @return 新品列表
     */
    List<ProductListItemVO> getNewProducts(Integer limit);

    /**
     * 获取促销商品
     * @param limit 数量限制
     * @return 促销商品列表
     */
    List<ProductListItemVO> getPromotionProducts(Integer limit);
    
    /**
     * 获取限时折扣商品
     * @param limit 数量限制
     * @return 限时折扣商品列表
     */
    List<ProductListItemVO> getDiscountProducts(Integer limit);
    
    /**
     * 获取优选好物商品
     * @param limit 数量限制
     * @return 优选好物商品列表
     */
    List<ProductListItemVO> getSelectedProducts(Integer limit);
} 