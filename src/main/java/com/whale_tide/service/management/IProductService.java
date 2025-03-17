package com.whale_tide.service.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.dto.management.product.*;
import com.whale_tide.entity.pms.PmsProductSkus;

import java.util.List;
/*
产品管理接口
*/

public interface IProductService {
    // 获取产品列表
    IPage<ProductListResult>  getProductList(ProductQueryParam queryParam);
    // 获取产品简单列表
    List<ProductSimpleResult> getProductSimple(String keyword);
    // 更新产品删除状态
    int updateDeleteStatus(UpdateDeleteStatusParam updateDeleteStatusParam);
    // 更新产品新品状态
    int updateNewStatus(UpdateNewStatusParam updateNewStatusParam);
    // 更新产品推荐状态
    int updateRecommendStatus(UpdateRecommendStatusParam updateRecommendStatusParam);
    // 更新产品上架状态
    int updatePublishStatusParam(UpdatePublishStatusParam updatePublishStatusParam);
    //创建产品
    int createProduct(ProductParam productCreateParam);
    // 获取商品编辑信息
    ProductParam getUpdateInfo(Long id);
    // 更新商品
    int updateProduct(Long id, ProductParam productParam);
    //获取商品SKU
    List<PmsProductSkus> getProductSkus(Long id);

}
