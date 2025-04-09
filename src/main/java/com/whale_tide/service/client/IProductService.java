package com.whale_tide.service.client;


import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.*;

import java.util.List;

/**
 * 客户端商品服务接口
 */
public interface IProductService {

    /**
     * 商品关键词搜索
     * 
     * @param request 搜索请求参数
     * @return 分页商品列表
     */
    PageResponse<ProductListItemResponse> getProductsByKeyword(ProductSearchRequest request);

    /**
     * 获取商品分类树
     * 
     * @return 商品分类树
     */
    CategoryTreeResponse getCategoryTree();

    /**
     * 获取商品详情
     * 
     * @param id 商品ID
     * @return 商品详情
     */
    ProductDetailResponse getProductDetail(Long id);

    /**
     * 获取热门搜索关键词
     * 
     * @return 热门搜索关键词列表
     */
    List<String> getHotKeywords();
    
    /**
     * 获取搜索建议
     * 
     * @param keyword 搜索关键词
     * @return 搜索建议列表
     */
    List<ProductSuggestionResponse> getSuggestions(String keyword);

}
