package com.whaletide.client.order.service.impl;

import com.whaletide.client.order.dto.ReviewSubmitDTO;
import com.whaletide.client.order.service.IOrderService;
import com.whaletide.client.order.service.IReviewService;
import com.whaletide.client.order.vo.OrderDetailVO;
import com.whaletide.client.order.vo.ReviewVO;
import com.whaletide.client.product.service.IProductService;
import com.whaletide.client.user.service.IUserService;
import com.whaletide.client.user.vo.UserVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.exception.BusinessException;
import com.whaletide.common.exception.NotFoundException;
import com.whaletide.common.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单评价服务实现
 */
@Service
public class ReviewServiceImpl implements IReviewService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IOrderService orderService;
    
    @Autowired
    private IProductService productService;
    
    /**
     * 提交评价
     *
     * @param reviewSubmitDTO 评价信息
     * @return 是否成功
     */
    @Override
    public Boolean submit(ReviewSubmitDTO reviewSubmitDTO) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        // 验证订单是否属于当前用户，且状态为已收货
        try {
            OrderDetailVO order = orderService.getOrderDetail(reviewSubmitDTO.getOrderId());
            if (order == null) {
                throw new NotFoundException("订单不存在");
            }
            
            // 检查订单是否属于当前用户
            if (!order.getUserId().equals(currentUser.getId())) {
                throw new UnauthorizedException("无权评价此订单");
            }
            
            // 检查订单状态是否为已收货
            if (!"3".equals(order.getStatus()) && !"已收货".equals(order.getStatusText())) {
                throw new BusinessException("订单尚未确认收货，无法评价");
            }
            
            // 检查订单商品ID是否有效
            boolean isOrderItemValid = order.getOrderItems().stream()
                .anyMatch(item -> item.getId().equals(reviewSubmitDTO.getOrderItemId()));
            if (!isOrderItemValid) {
                throw new NotFoundException("订单商品不存在");
            }
            
            // 检查商品ID是否匹配
            boolean isProductValid = order.getOrderItems().stream()
                .anyMatch(item -> item.getId().equals(reviewSubmitDTO.getOrderItemId()) 
                    && item.getProductId().equals(reviewSubmitDTO.getProductId()));
            if (!isProductValid) {
                throw new BusinessException("商品信息不匹配");
            }
            
            // 检查是否已评价过
            // 实际项目中，这里应该查询数据库检查是否已评价
            // 这里简化处理
            
            // 保存评价
            // 实际项目中，这里应该将评价保存到数据库
            LOGGER.info("用户:{} 提交商品:{} 评价成功", currentUser.getId(), reviewSubmitDTO.getProductId());
            return true;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("提交评价失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取商品评价列表
     *
     * @param productId 商品ID
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 评价分页列表
     */
    @Override
    public CommonPage<ReviewVO> listByProduct(Long productId, Integer pageNum, Integer pageSize) {
        try {
            // 检查商品是否存在
            try {
                // 这里使用产品服务获取商品详情
                productService.getProductDetail(productId);
            } catch (Exception e) {
                LOGGER.error("获取商品评价失败，商品不存在: {}", productId, e);
                throw new NotFoundException("商品不存在");
            }
            
            // 实际项目中，这里应该查询数据库获取评价列表
            // 这里是模拟实现
            List<ReviewVO> reviewList = new ArrayList<>();
            
            // 模拟数据
            for (int i = 0; i < 10; i++) {
                ReviewVO review = new ReviewVO();
                review.setId((long) (i + 1));
                review.setOrderId((long) (3000 + i));
                review.setProductId(productId);
                review.setProductName("测试商品" + productId);
                review.setProductPic("http://example.com/product" + productId + ".jpg");
                review.setUserId((long) (1000 + i));
                review.setNickname("用户" + (1000 + i));
                review.setAvatar("http://example.com/avatar" + (1000 + i) + ".jpg");
                review.setContent("这是第 " + (i + 1) + " 条评价内容，商品很好用，非常满意！");
                review.setStar(i % 5 == 0 ? 4 : 5); // 大部分是5星
                review.setAnonymous(i % 3 == 0);
                
                // 评价图片
                if (i % 2 == 0) {
                    List<String> pics = new ArrayList<>();
                    for (int j = 0; j < (i % 3) + 1; j++) {
                        pics.add("http://example.com/review" + i + "_" + j + ".jpg");
                    }
                    review.setPics(pics);
                }
                
                review.setCreateTime(new Date());
                reviewList.add(review);
            }
            
            // 创建分页对象
            return createPageResult(reviewList, pageNum, pageSize);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("获取商品评价列表失败: {}", e.getMessage(), e);
            return new CommonPage<>();
        }
    }
    
    /**
     * 获取用户评价列表
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 评价分页列表
     */
    @Override
    public CommonPage<ReviewVO> listByUser(Integer pageNum, Integer pageSize) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        try {
            // 实际项目中，这里应该查询数据库获取用户评价列表
            // 这里是模拟实现
            List<ReviewVO> reviewList = new ArrayList<>();
            
            // 模拟数据
            for (int i = 0; i < 8; i++) {
                ReviewVO review = new ReviewVO();
                review.setId((long) (i + 1));
                review.setOrderId((long) (3000 + i));
                review.setProductId((long) (2000 + i));
                review.setProductName("我的评价商品" + (i + 1));
                review.setProductPic("http://example.com/my_product" + (i + 1) + ".jpg");
                review.setUserId(currentUser.getId());
                review.setNickname(currentUser.getUsername());
                review.setAvatar(currentUser.getAvatar());
                review.setContent("这是我的第 " + (i + 1) + " 条评价，商品质量不错，物流很快！");
                review.setStar(i % 4 == 0 ? 4 : 5); // 大部分是5星
                review.setAnonymous(i % 5 == 0);
                
                // 评价图片
                if (i % 2 == 0) {
                    List<String> pics = new ArrayList<>();
                    for (int j = 0; j < (i % 3) + 1; j++) {
                        pics.add("http://example.com/my_review" + i + "_" + j + ".jpg");
                    }
                    review.setPics(pics);
                }
                
                review.setCreateTime(new Date());
                reviewList.add(review);
            }
            
            // 创建分页对象
            return createPageResult(reviewList, pageNum, pageSize);
        } catch (Exception e) {
            LOGGER.error("获取用户评价列表失败: {}", e.getMessage(), e);
            return new CommonPage<>();
        }
    }
    
    /**
     * 获取评价详情
     *
     * @param id 评价ID
     * @return 评价详情
     */
    @Override
    public ReviewVO getDetail(Long id) {
        // 实际项目中，这里应该查询数据库获取评价详情
        try {
            // 模拟评价数据
            ReviewVO review = new ReviewVO();
            review.setId(id);
            review.setOrderId((long) (3000));
            review.setProductId((long) (2000));
            review.setProductName("评价详情商品");
            review.setProductPic("http://example.com/detail_product.jpg");
            review.setUserId((long) (1000));
            review.setNickname("用户1000");
            review.setAvatar("http://example.com/avatar1000.jpg");
            review.setContent("这是评价 ID 为 " + id + " 的详细内容，商品很好用，非常满意！");
            review.setStar(5);
            review.setAnonymous(false);
            
            // 评价图片
            List<String> pics = new ArrayList<>();
            pics.add("http://example.com/detail_review1.jpg");
            pics.add("http://example.com/detail_review2.jpg");
            review.setPics(pics);
            
            review.setCreateTime(new Date());
            
            return review;
        } catch (Exception e) {
            LOGGER.error("获取评价详情失败: {}", e.getMessage(), e);
            throw new NotFoundException("评价不存在");
        }
    }
    
    /**
     * 删除评价
     *
     * @param id 评价ID
     * @return 是否成功
     */
    @Override
    public Boolean delete(Long id) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        try {
            // 获取评价详情，检查是否属于当前用户
            // 实际项目中，这里应该查询数据库获取评价详情
            ReviewVO review = getDetail(id);
            
            // 检查评价是否属于当前用户
            if (!review.getUserId().equals(currentUser.getId())) {
                throw new UnauthorizedException("无权删除此评价");
            }
            
            // 删除评价
            // 实际项目中，这里应该从数据库删除评价
            LOGGER.info("用户:{} 删除评价:{} 成功", currentUser.getId(), id);
            return true;
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("删除评价失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 创建分页结果
     *
     * @param list     列表数据
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    private CommonPage<ReviewVO> createPageResult(List<ReviewVO> list, Integer pageNum, Integer pageSize) {
        // 分页处理
        int total = list.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        
        List<ReviewVO> pageList;
        if (startIndex < total) {
            pageList = list.subList(startIndex, endIndex);
        } else {
            pageList = new ArrayList<>();
        }
        
        // 创建分页对象
        CommonPage<ReviewVO> result = new CommonPage<>();
        result.setList(pageList);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal((long)total);
        result.setTotalPage((int) Math.ceil(total / (double) pageSize));
        
        return result;
    }
} 