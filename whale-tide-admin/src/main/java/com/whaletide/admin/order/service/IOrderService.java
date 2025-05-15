package com.whaletide.admin.order.service;


import com.whaletide.admin.order.vo.OrderDetailVO;
import com.whaletide.admin.order.vo.OrderListVO;
import com.whaletide.admin.order.vo.OrderStatsticsVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单相关业务接口
 */
public interface IOrderService {
    /**
     * 获取订单列表
     */
    OrderListVO getOrderList(Integer pageNum, Integer pageSize, Long userId, Integer status);

    /**
     * 获取订单详情
     */
    OrderDetailVO getOrderDetail(Long orderId);

    /**
     * 订单统计
     */
    OrderStatsticsVO statistics(LocalDate startTime, LocalDate endTime, String type);
}
