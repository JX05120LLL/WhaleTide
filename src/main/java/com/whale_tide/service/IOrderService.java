package com.whale_tide.service;

import com.whale_tide.dto.CloseOrderParam;
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
     * @param closeOrderParam 关闭订单参数
     * @return 关闭成功的订单数量
     */
    int closeOrder(CloseOrderParam closeOrderParam);
    
    /**
     * 订单详情
     */

}
