package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.coupon.ProductCouponResponse;
import com.whale_tide.dto.client.coupon.UserCouponListRequest;
import com.whale_tide.dto.client.coupon.UserCouponResponse;
import com.whale_tide.service.client.ICouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券接口控制器
 */
@Slf4j
@RestController
@Api(tags = "优惠券相关接口")
@RequestMapping("/member/coupon")
public class CouponController {

    @Autowired
    private ICouponService couponService;

    @ApiOperation("获取可领取优惠券列表")
    @GetMapping("/list")
    public CommonResult<PageResponse<UserCouponResponse>> getCouponList(UserCouponListRequest request) {
        PageResponse<UserCouponResponse> response = couponService.getCouponList(request);
        return CommonResult.success(response);

    }

    @ApiOperation("领取优惠券")
    @PostMapping("/add/{couponId}")
    public CommonResult<Void> receiveCoupon(@PathVariable Long couponId) {
        couponService.receiveCoupon(couponId);
        return CommonResult.success(null, "领取成功");

    }

    @ApiOperation("获取商品可用优惠券")
    @GetMapping("/listByProduct/{productId}")
    public CommonResult<List<ProductCouponResponse>> getProductCoupon(@PathVariable Long productId) {
        List<ProductCouponResponse> response = couponService.getProductCoupon(productId);
        return CommonResult.success(response);
    }
} 