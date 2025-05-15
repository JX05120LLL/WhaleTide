package com.whaletide.client.product.service;

import com.whaletide.client.product.dto.ProductCommentAddDTO;
import com.whaletide.client.product.dto.ProductCommentQueryDTO;
import com.whaletide.client.product.vo.ProductCommentVO;
import com.whaletide.common.api.CommonPage;

/**
 * 商品评论服务接口
 */
public interface IProductCommentService {
    
    /**
     * 获取商品评论列表
     * @param queryDTO 查询条件
     * @return 评论分页列表
     */
    ProductCommentVO getProductCommentList(ProductCommentQueryDTO queryDTO);
    
    /**
     * 添加商品评论
     * @param commentDTO 评论信息
     */
    void addProductComment(ProductCommentAddDTO commentDTO);
} 