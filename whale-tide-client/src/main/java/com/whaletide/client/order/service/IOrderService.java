package com.whaletide.client.order.service;

import com.whaletide.client.order.dto.*;
import com.whaletide.client.order.vo.*;
import com.whaletide.common.api.CommonPage;

/**
 * 订单服务接口
 */
public interface IOrderService {
    
    /**
     * 生成确认订单
     * @param request 生成确认订单请求
     * @return 确认订单信息
     */
    ConfirmOrderVO generateConfirmOrder(GenerateConfirmOrderDTO request);
    
    /**
     * 生成订单
     * @param request 生成订单请求
     * @return 订单生成结果
     */
    GenerateOrderVO generateOrder(GenerateOrderDTO request);
    
    /**
     * 获取订单列表
     * @param request 订单列表请求
     * @return 订单列表分页结果
     */
    CommonPage<OrderListItemVO> getOrderList(OrderListQueryDTO request);
    
    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailVO getOrderDetail(Long orderId);
    
    /**
     * 取消订单
     * @param request 取消订单请求
     * @return 是否成功
     */
    Boolean cancelOrder(CancelOrderDTO request);
    
    /**
     * 确认收货
     * @param request 确认收货请求
     * @return 是否成功
     */
    Boolean confirmReceiveOrder(ConfirmReceiveDTO request);
    
    /**
     * 删除订单
     * @param request 删除订单请求
     * @return 是否成功
     */
    Boolean deleteOrder(DeleteOrderDTO request);
    
    /**
     * 支付成功处理（简化版）
     * @param request 支付成功请求
     * @return 支付成功响应
     */
    PaySuccessVO paySuccess(PaySuccessDTO request);
    
    /**
     * 直接购买商品
     * @param request 直接购买请求
     * @return 购物项ID
     */
    Long directBuy(DirectBuyDTO request);
} 