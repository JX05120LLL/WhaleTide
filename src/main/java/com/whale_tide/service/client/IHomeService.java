package com.whale_tide.service.client;

import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.home.HomeContentResponse;
import com.whale_tide.dto.client.home.HomeProductRequest;
import com.whale_tide.dto.client.product.ProductListItemResponse;

/**
 * 首页内容Service接口
 */
public interface IHomeService {
    
    /**
     * 获取首页内容
     * @return 首页内容
     */
    HomeContentResponse getHomeContent();
    
    /**
     * 获取首页推荐商品列表
     * @param request 请求参数
     * @return 推荐商品列表
     */
    PageResponse<ProductListItemResponse> getRecommendProductList(HomeProductRequest request);
    
    /**
     * 获取首页新品列表
     * @param request 请求参数
     * @return 新品列表
     */
    PageResponse<ProductListItemResponse> getNewProductList(HomeProductRequest request);
    
    /**
     * 获取首页热卖商品列表
     * @param request 请求参数
     * @return 热卖商品列表
     */
    PageResponse<ProductListItemResponse> getHotProductList(HomeProductRequest request);
} 