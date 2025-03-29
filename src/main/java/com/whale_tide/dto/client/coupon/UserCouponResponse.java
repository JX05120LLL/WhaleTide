package com.whale_tide.dto.client.coupon;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;



@Data
@ApiModel("获取用户优惠券列表响应参数")
public class UserCouponResponse {
    @ApiModelProperty("UmsUserCoupons 主键 ID")
    private Long id;
    @ApiModelProperty("优惠券ID")
    private Long couponId;
    @ApiModelProperty("优惠券名称")
    private String name;
    @ApiModelProperty("优惠券数量")
    private BigDecimal amount;
    @ApiModelProperty("最低使用积分")
    private BigDecimal minPoint;
    @ApiModelProperty("优惠券开始时间")
    private Date startTime;
    @ApiModelProperty("优惠券结束时间")
    private Date endTime;
    @ApiModelProperty("优惠券状态，0->未使用；1->已使用；2->已过期")
    private Integer useStatus;

}
