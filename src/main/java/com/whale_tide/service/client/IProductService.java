package com.whale_tide.service.client;


import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.*;

public interface IProductService {

    //商品关键词搜索
    PageResponse<ProductListItemResponse> getProductsByKeyword(ProductSearchRequest request);

    //获取商品分类树
    CategoryTreeResponse getCategoryTree();

    // 获取商品详情
    ProductDetailResponse getProductDetail(Long id);





}
