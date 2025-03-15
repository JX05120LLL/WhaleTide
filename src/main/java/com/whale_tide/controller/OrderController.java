package com.whale_tide.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.order.*;
import com.whale_tide.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理控制器
 */
@RestController
@Api(tags = "OrderController", description = "订单管理")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<OrderResult>> list(OrderQueryParam queryParam) {
        IPage<OrderResult> orderList = orderService.getOrderList(queryParam);
        return CommonResult.success(CommonPage.restPage(orderList));
    }
    
    @ApiOperation("关闭订单")
    @PostMapping("/update/close")
    public CommonResult<Integer> close(@RequestBody CloseOrderParam closeOrderParam) {
        int count = orderService.closeOrder(closeOrderParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed("没有订单被关闭");
    }
    
    // 删除订单
    @ApiOperation("删除订单")
    @PostMapping("/delete")
    public CommonResult<Integer> delete(@RequestBody DeleteOrderParam deleteOrderParam) {
        int count = orderService.deleteOrder(deleteOrderParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed("没有订单被删除");
    }

    // 发货
    @ApiOperation("发货")
    @PostMapping("/update/delivery")
    public CommonResult<Integer> deliver(@RequestBody OrderDeliveryParam orderDeliveryParam) {
        int count = orderService.deliver(orderDeliveryParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed("没有订单被发货");
    }

    // 获取订单详情
    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public CommonResult<OrderDetailResult> detail(@PathVariable Long id) {
        OrderDetailResult orderDetail = orderService.getOrderDetail(id);
        if (orderDetail != null) {
            return CommonResult.success(orderDetail);
        }
        return CommonResult.failed("订单不存在");
    }

    // 更新收货人信息
    @ApiOperation("更新收货人信息")
    @PostMapping("/update/receiverInfo")
    public CommonResult<Integer> updateReceiverInfo(@RequestBody ReceiverInfoParam receiverInfoParam) {
        int count = orderService.updateReceiverInfo(receiverInfoParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed("更新收货人信息失败");
    }

    



} 