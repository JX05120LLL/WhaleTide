package com.whale_tide.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.OrderQueryParam;
import com.whale_tide.dto.OrderResult;
import com.whale_tide.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    // 其他订单相关接口...
} 