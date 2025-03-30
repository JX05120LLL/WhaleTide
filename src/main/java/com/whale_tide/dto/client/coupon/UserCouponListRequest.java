package com.whale_tide.dto.client.coupon;


import com.whale_tide.common.api.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取可领取优惠券列表请求参数
 */

@Data
@ApiModel("获取可领取优惠券列表请求参数")
public class UserCouponListRequest extends PageRequest {

    @ApiModelProperty("使用状态 0:未使用 1:已使用 2:已过期")
    private Integer useStatus;

}
