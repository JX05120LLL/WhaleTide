package com.whaletide.client.order.service;

import com.whaletide.client.order.dto.ReviewSubmitDTO;
import com.whaletide.client.order.vo.ReviewVO;
import com.whaletide.common.api.CommonPage;

/**
 * 订单评价服务接口
 */
public interface IReviewService {
    
    /**
     * 提交评价
     * @param reviewSubmitDTO 评价信息
     * @return 是否成功
     */
    Boolean submit(ReviewSubmitDTO reviewSubmitDTO);
    
    /**
     * 获取商品评价列表
     * @param productId 商品ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 评价分页列表
     */
    CommonPage<ReviewVO> listByProduct(Long productId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取用户评价列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 评价分页列表
     */
    CommonPage<ReviewVO> listByUser(Integer pageNum, Integer pageSize);
    
    /**
     * 获取评价详情
     * @param id 评价ID
     * @return 评价详情
     */
    ReviewVO getDetail(Long id);
    
    /**
     * 删除评价
     * @param id 评价ID
     * @return 是否成功
     */
    Boolean delete(Long id);
} 