package com.whaletide.client.order.controller;

import com.whaletide.client.order.dto.ReviewSubmitDTO;
import com.whaletide.client.order.service.IReviewService;
import com.whaletide.client.order.vo.ReviewVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单评价控制器
 */
@RestController
@RequestMapping("/api/review")
@Tag(name = "订单评价管理", description = "订单评价相关接口")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @PostMapping("/submit")
    @Operation(summary = "提交评价")
    public CommonResult<Boolean> submit(@RequestBody ReviewSubmitDTO reviewSubmitDTO) {
        return CommonResult.success(reviewService.submit(reviewSubmitDTO));
    }

    @GetMapping("/list/product/{productId}")
    @Operation(summary = "获取商品评价列表")
    public CommonResult<CommonPage<ReviewVO>> listByProduct(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return CommonResult.success(reviewService.listByProduct(productId, pageNum, pageSize));
    }

    @GetMapping("/list/user")
    @Operation(summary = "获取用户评价列表")
    public CommonResult<CommonPage<ReviewVO>> listByUser(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return CommonResult.success(reviewService.listByUser(pageNum, pageSize));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "获取评价详情")
    public CommonResult<ReviewVO> getDetail(@PathVariable("id") Long id) {
        return CommonResult.success(reviewService.getDetail(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除评价")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {
        return CommonResult.success(reviewService.delete(id));
    }
} 