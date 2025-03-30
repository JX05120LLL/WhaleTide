package com.whale_tide.dto.client.coupon;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 获取商品可用有优惠券 响应参数
 */
@Data
@ApiModel("获取商品可用有优惠券 响应参数")
public class ProductCouponResponse {
    @ApiModelProperty("UmsUserCoupons 主键 ID")
    private Long id;
    @ApiModelProperty("优惠券名称")
    private String name;
    @ApiModelProperty("优惠券数量")
    private BigDecimal amount;
    @ApiModelProperty("优惠券有效期")
    private BigDecimal minPoint;
    @ApiModelProperty("优惠券开始时间")
    private Date startTime;
    @ApiModelProperty("优惠券结束时间")
    private Date endTime;
    @ApiModelProperty("优惠券类型")
    private Integer useType; // 0->全场通用；1->指定品类；2->指定商品



}
