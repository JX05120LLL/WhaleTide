package com.whale_tide.service.client;


import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.*;

public interface IBrandService {

    // 获取品牌详情
    BrandDetailResponse getBrandDetail(Long id);

    // 获取推荐品牌列表，需要分页
    PageResponse<BrandListItemResponse> getRecommendBrands(RecommendBrandRequest request);


    // 获取品牌商品列表，需要分页
    PageResponse<ProductListItemResponse> getBrandProducts(BrandProductRequest request);
}
