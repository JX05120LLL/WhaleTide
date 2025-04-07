package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.order.*;
import com.whale_tide.service.client.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 */
@Slf4j
@Api(tags = "订单相关接口")
@RestController("clientOrderController")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 生成确认订单
     */
    @ApiOperation("生成确认订单")
    @PostMapping("/generateConfirmOrder")
    public CommonResult<ConfirmOrderResponse> generateConfirmOrder(@RequestBody GenerateConfirmOrderRequest request) {
        try {
            ConfirmOrderResponse response = orderService.generateConfirmOrder(request);
            return CommonResult.success(response);
        } catch (Exception e) {
            log.error("生成确认订单异常: {}", e.getMessage(), e);
            return CommonResult.failed("生成确认订单失败: " + e.getMessage());
        }
    }

    /**
     * 提交订单
     */
    @ApiOperation("生成订单")
    @PostMapping("/generateOrder")
    public CommonResult<GenerateOrderResponse> generateOrder(@RequestBody GenerateOrderRequest request) {
        try {
            GenerateOrderResponse response = orderService.generateOrder(request);
            return CommonResult.success(response);
        } catch (Exception e) {
            log.error("生成订单异常: {}", e.getMessage(), e);
            return CommonResult.failed("生成订单失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单列表
     */
    @ApiOperation("获取订单列表")
    @GetMapping("/get/orderList")
    public CommonResult<PageResponse<OrderListResponse>> getOrderList(OrderListRequest request) {
        try {
            PageResponse<OrderListResponse> response = orderService.getOrderList(request);
            return CommonResult.success(response);
        } catch (Exception e) {
            log.error("获取订单列表异常: {}", e.getMessage(), e);
            return CommonResult.failed("获取订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    @ApiOperation("获取订单详情")
    @GetMapping("/detail/{orderId}")
    public CommonResult<OrderDetailResponse> getOrderDetail(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            OrderDetailResponse response = orderService.getOrderDetail(orderId);
            return CommonResult.success(response);
        } catch (Exception e) {
            log.error("获取订单详情异常: {}", e.getMessage(), e);
            return CommonResult.failed("获取订单详情失败: " + e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @ApiOperation("取消订单")
    @PostMapping("/cancelUserOrder")
    public CommonResult<Boolean> cancelOrder(@RequestBody CancelOrderRequest request) {
        try {
            boolean result = orderService.cancelOrder(request);
            if (result) {
                return CommonResult.success(true, "取消订单成功");
            } else {
                return CommonResult.failed("取消订单失败");
            }
        } catch (Exception e) {
            log.error("取消订单异常: {}", e.getMessage(), e);
            return CommonResult.failed("取消订单失败: " + e.getMessage());
        }
    }

    /**
     * 确认收货
     */
    @ApiOperation("确认收货")
    @PostMapping("/confirmReceiveOrder")
    public CommonResult<Boolean> confirmReceiveOrder(@RequestBody ConfirmReceiveRequest request) {
        try {
            boolean result = orderService.confirmReceiveOrder(request);
            if (result) {
                return CommonResult.success(true, "确认收货成功");
            } else {
                return CommonResult.failed("确认收货失败");
            }
        } catch (Exception e) {
            log.error("确认收货异常: {}", e.getMessage(), e);
            return CommonResult.failed("确认收货失败: " + e.getMessage());
        }
    }

    /**
     * 删除订单
     */
    @ApiOperation("删除订单")
    @PostMapping("/deleteOrder")
    public CommonResult<Boolean> deleteOrder(@RequestBody DeleteOrderRequest request) {
        try {
            boolean result = orderService.deleteOrder(request);
            if (result) {
                return CommonResult.success(true, "删除订单成功");
            } else {
                return CommonResult.failed("删除订单失败");
            }
        } catch (Exception e) {
            log.error("删除订单异常: {}", e.getMessage(), e);
            return CommonResult.failed("删除订单失败: " + e.getMessage());
        }
    }

    /**
     * 支付成功回调
     */
    @ApiOperation("支付成功回调")
    @PostMapping("/paySuccess")
    public CommonResult<PaySuccessResponse> paySuccess(@RequestBody PaySuccessRequest request) {
        try {
            PaySuccessResponse response = orderService.paySuccess(request);
            return CommonResult.success(response, "支付成功");
        } catch (Exception e) {
            log.error("支付成功回调异常: {}", e.getMessage(), e);
            return CommonResult.failed("处理支付回调失败: " + e.getMessage());
        }
    }

    /**
     * 查询支付宝订单状态
     */
    @ApiOperation("查询支付宝订单状态")
    @GetMapping("/query")
    public CommonResult<AlipayQueryResponse> queryAlipayStatus(AlipayQueryRequest request) {
        try {
            AlipayQueryResponse response = orderService.queryAlipayStatus(request);
            return CommonResult.success(response);
        } catch (Exception e) {
            log.error("查询支付宝订单状态异常: {}", e.getMessage(), e);
            return CommonResult.failed("查询支付宝订单状态失败: " + e.getMessage());
        }
    }

    /**
     * 直接购买商品
     */
    @ApiOperation("直接购买商品")
    @PostMapping("/directBuy")
    public CommonResult<Long> directBuy(@RequestBody DirectBuyRequest request) {
        try {
            Long cartItemId = orderService.directBuy(request);
            return CommonResult.success(cartItemId, "创建临时购物项成功");
        } catch (Exception e) {
            log.error("直接购买商品异常: {}", e.getMessage(), e);
            return CommonResult.failed("直接购买失败: " + e.getMessage());
        }
    }
} 