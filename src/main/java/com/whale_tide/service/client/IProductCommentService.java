package com.whale_tide.service.client;

import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.ProductCommentAddRequest;
import com.whale_tide.dto.client.product.ProductCommentResponse;
import com.whale_tide.dto.client.product.ProductCommentParam;

public interface IProductCommentService {
    PageResponse<ProductCommentResponse> getProductCommentList(ProductCommentParam queryParam);

    void addProductComment(ProductCommentAddRequest request);
}
