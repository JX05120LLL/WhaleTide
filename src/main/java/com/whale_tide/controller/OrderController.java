package com.whale_tide.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.CloseOrderParam;
import com.whale_tide.dto.OrderQueryParam;
import com.whale_tide.dto.OrderResult;
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
    
    // 其他订单相关接口...
} 