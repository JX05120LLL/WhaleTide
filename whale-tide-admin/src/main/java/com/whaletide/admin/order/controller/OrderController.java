package com.whaletide.admin.order.controller;


import com.sun.org.apache.xpath.internal.operations.Or;
import com.whaletide.admin.order.service.IOrderService;
import com.whaletide.admin.order.vo.OrderDetailVO;
import com.whaletide.admin.order.vo.OrderListVO;
import com.whaletide.admin.order.vo.OrderStatsticsVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/admin/order")
@Tag(name = "订单相关接口")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Operation(summary = "获取订单列表")
    @GetMapping("/list")
    public CommonResult<OrderListVO> list(@RequestParam(required = false) Integer pageNum,
                                          @RequestParam(required = false) Integer pageSize,
                                          @RequestParam(required = false) Long userId,
                                          @RequestParam(required = false) Integer status) {

        OrderListVO orderListVO = orderService.getOrderList(pageNum, pageSize, userId, status);
        return CommonResult.success(orderListVO);
    }
    
    @Operation(summary = "获取订单详情")
    @GetMapping("/detail/{id}")
    public CommonResult<OrderDetailVO> detail(@PathVariable Long id) {
        OrderDetailVO orderDetail = orderService.getOrderDetail(id);
        return CommonResult.success(orderDetail);
    }

    /**
     * 订单统计
     */
    @Operation(summary = "订单统计")
    @GetMapping("/statistics")
    public CommonResult<OrderStatsticsVO> statistics(@RequestParam("startDate") String startDate,
                                       @RequestParam("endDate") String endDate,
                                       @RequestParam("type") String type) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        OrderStatsticsVO orderStatsticsVO = orderService.statistics(start, end, type);

        return CommonResult.success(orderStatsticsVO);
    }

}
