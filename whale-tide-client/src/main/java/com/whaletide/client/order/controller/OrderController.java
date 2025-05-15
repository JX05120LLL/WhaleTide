package com.whaletide.client.order.controller;

import com.whaletide.client.order.dto.*;
import com.whaletide.client.order.service.IOrderService;
import com.whaletide.client.order.vo.*;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
@Tag(name = "订单相关接口")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 生成确认订单
     */
    @Operation(summary = "生成确认订单")
    @PostMapping("/generateConfirmOrder")
    public CommonResult<ConfirmOrderVO> generateConfirmOrder(@RequestBody GenerateConfirmOrderDTO request) {
        ConfirmOrderVO response = orderService.generateConfirmOrder(request);
        return CommonResult.success(response);
    }

    /**
     * 提交订单
     */
    @Operation(summary = "生成订单")
    @PostMapping("/generateOrder")
    public CommonResult<GenerateOrderVO> generateOrder(@RequestBody GenerateOrderDTO request) {
        GenerateOrderVO response = orderService.generateOrder(request);
        return CommonResult.success(response);
    }

    /**
     * 获取订单列表
     */
    @Operation(summary = "获取订单列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<OrderListItemVO>> getOrderList(OrderListQueryDTO request) {
        CommonPage<OrderListItemVO> response = orderService.getOrderList(request);
        return CommonResult.success(response);
    }

    /**
     * 获取订单详情
     */
    @Operation(summary = "获取订单详情")
    @GetMapping("/detail/{orderId}")
    public CommonResult<OrderDetailVO> getOrderDetail(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        OrderDetailVO response = orderService.getOrderDetail(orderId);
        return CommonResult.success(response);
    }

    /**
     * 取消订单
     */
    @Operation(summary = "取消订单")
    @PostMapping("/cancel")
    public CommonResult<Boolean> cancelOrder(@RequestBody CancelOrderDTO request) {
        Boolean result = orderService.cancelOrder(request);
        return CommonResult.success(result, "取消订单成功");
    }

    /**
     * 确认收货
     */
    @Operation(summary = "确认收货")
    @PostMapping("/confirmReceive")
    public CommonResult<Boolean> confirmReceiveOrder(@RequestBody ConfirmReceiveDTO request) {
        Boolean result = orderService.confirmReceiveOrder(request);
        return CommonResult.success(result, "确认收货成功");
    }

    /**
     * 删除订单
     */
    @Operation(summary = "删除订单")
    @PostMapping("/delete")
    public CommonResult<Boolean> deleteOrder(@RequestBody DeleteOrderDTO request) {
        Boolean result = orderService.deleteOrder(request);
        return CommonResult.success(result, "删除订单成功");
    }

    /**
     * 支付成功回调
     */
    @Operation(summary = "支付成功回调")
    @PostMapping("/paySuccess")
    public CommonResult<PaySuccessVO> paySuccess(@RequestBody PaySuccessDTO request) {
        PaySuccessVO response = orderService.paySuccess(request);
        return CommonResult.success(response, "支付成功");
    }


    /**
     * 直接购买商品
     */
    @Operation(summary = "直接购买商品")
    @PostMapping("/directBuy")
    public CommonResult<Long> directBuy(@RequestBody DirectBuyDTO request) {
        Long cartItemId = orderService.directBuy(request);
        return CommonResult.success(cartItemId, "创建临时购物项成功");
    }
} 