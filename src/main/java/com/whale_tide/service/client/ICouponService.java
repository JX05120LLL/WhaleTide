package com.whale_tide.service.client;


import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.coupon.ProductCouponResponse;
import com.whale_tide.dto.client.coupon.UserCouponListRequest;
import com.whale_tide.dto.client.coupon.UserCouponResponse;

import java.util.List;

/**
 * 优惠券服务接口
 */


public interface ICouponService {

    // 获取优惠券列表
    PageResponse<UserCouponResponse> getCouponList(UserCouponListRequest request);
    // 领取优惠券
    void receiveCoupon(Long couponId);
    // 获取商品可用优惠券
    List<ProductCouponResponse> getProductCoupon(Long productId);





}
