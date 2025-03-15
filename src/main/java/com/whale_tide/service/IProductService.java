package com.whale_tide.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.dto.product.*;
/*
产品管理接口
*/

public interface IProductService {

    IPage<ProductListResult>  getProductList(ProductQueryParam queryParam);

    ProductSimpleResult getProductSimple(String keyword);

    int updateDeleteStatus(UpdateDeleteStatusParam updateDeleteStatusParam);

    int updateNewStatus(UpdateNewStatusParam updateNewStatusParam);

    int updateRecommendStatus(UpdateRecommendStatusParam updateRecommendStatusParam);

    int updatePublishStatusParam(UpdatePublishStatusParam updatePublishStatusParam);
    //创建产品
    int createProduct(ProductParam productCreateParam);



}
