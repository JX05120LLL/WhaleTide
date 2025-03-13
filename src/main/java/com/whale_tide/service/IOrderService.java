package com.whale_tide.service;

import com.whale_tide.entity.OmsOrders;
import com.whale_tide.dto.OrderQueryParam;
import com.whale_tide.dto.OrderResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 订单管理接口
 */
public interface IOrderService {

    /**
     * 获取订单列表
     * @param queryParam 查询参数
     * @return 订单列表分页信息
     */
    IPage<OrderResult> getOrderList(OrderQueryParam queryParam);
    
    /**
     * 关闭订单
     */
    void closeOrder();
    
    /**
     * 订单详情
     */
    // 其他接口方法...
}
