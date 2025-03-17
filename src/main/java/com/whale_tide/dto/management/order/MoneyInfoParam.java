package com.whale_tide.dto.management.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新订单费用信息 请求参数
 */

@Data
public class MoneyInfoParam {

    @ApiModelProperty("订单ID")
    private Long OrderId;

    @ApiModelProperty("运费金额")
    private Double freightAmount;

    @ApiModelProperty("优惠金额")
    private Double discountAmount;

    @ApiModelProperty("订单状态")
    private int status;





}
