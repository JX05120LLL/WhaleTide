package com.whale_tide.service;

import com.whale_tide.dto.order.*;
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
     * 删除订单
     * @param deleteOrderParam 删除订单参数
     * @return 删除成功的订单数量
     */
    int deleteOrder(DeleteOrderParam deleteOrderParam);

    /**
     * 发货
     * @param orderDeliveryParam 订单发货参数
     * @return 成功发货的订单数量
     */
    int deliver(OrderDeliveryParam orderDeliveryParam);

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailResult getOrderDetail(Long orderId);

    /**
     *更新收货人信息
     * @param receiverInfoParam 收货人信息参数
     * @return 更新成功的订单数量
     */
    int updateReceiverInfo(ReceiverInfoParam receiverInfoParam);

    /**
     *更新订单费用信息
     * @param moneyInfoParam 订单费用信息参数
     * @return 更新成功的订单数量
     */
    int updateOrderAmount(MoneyInfoParam moneyInfoParam);

    /**
     * 更新订单备注信息
     * @param orderNoteParam
     * @return
     */
    int updateOrderNote(OrderNoteParam orderNoteParam);

}
