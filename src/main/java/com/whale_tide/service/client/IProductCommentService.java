package com.whale_tide.service.client;

import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.ProductCommentAddRequest;
import com.whale_tide.dto.client.product.ProductCommentResponse;
import com.whale_tide.dto.client.product.ProductCommentParam;
/**
 * 商品评论服务接口
 */
public interface IProductCommentService {
    /**
     * 获取商品评论列表
     * @param queryParam 查询参数
     * @return 商品评论列表
     */
    PageResponse<ProductCommentResponse> getProductCommentList(ProductCommentParam queryParam);
    /**
     * 添加商品评论
     * @param request 评论请求
     */
    void addProductComment(ProductCommentAddRequest request);
}
