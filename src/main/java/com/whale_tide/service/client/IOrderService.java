package com.whale_tide.service.client;

import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.order.*;

import java.util.List;

/**
 * 客户端订单服务接口
 */
public interface IOrderService {

    /**
     * 生成确认订单
     *
     * @param request 生成确认订单请求
     * @return 确认订单响应
     */
    ConfirmOrderResponse generateConfirmOrder(GenerateConfirmOrderRequest request);

    /**
     * 生成订单
     *
     * @param request 生成订单请求
     * @return 生成订单响应
     */
    GenerateOrderResponse generateOrder(GenerateOrderRequest request);

    /**
     * 获取订单列表
     *
     * @param request 订单列表请求
     * @return 订单列表
     */
    PageResponse<OrderListResponse> getOrderList(OrderListRequest request);

    /**
     * 支付成功回调
     *
     * @param request 支付成功回调请求
     * @return 支付成功回调响应
     */
    PaySuccessResponse paySuccess(PaySuccessRequest request);

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailResponse getOrderDetail(Long orderId);

    /**
     * 取消订单
     *
     * @param request 取消订单请求
     * @return 操作是否成功
     */
    boolean cancelOrder(CancelOrderRequest request);

    /**
     * 确认收货
     *
     * @param request 确认收货请求
     * @return 操作是否成功
     */
    boolean confirmReceiveOrder(ConfirmReceiveRequest request);

    /**
     * 删除订单
     *
     * @param request 删除订单请求
     * @return 操作是否成功
     */
    boolean deleteOrder(DeleteOrderRequest request);

    /**
     * 支付宝订单状态查询
     *
     * @param request 支付宝查询请求
     * @return 支付宝查询响应
     */
    AlipayQueryResponse queryAlipayStatus(AlipayQueryRequest request);

    /**
     * 直接购买商品
     *
     * @param request 直接购买请求
     * @return 临时购物车项ID
     */
    Long directBuy(DirectBuyRequest request);
} 