package com.whale_tide.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单发货请求参数
 * @author Bro_cat
 * 2025-03-14
 */

@Data
public class OrderDeliveryParam {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("物流公司")
    private String deliveryCompany;

    @ApiModelProperty("物流单号")
    private String deliverySn;

    @ApiModelProperty("发货商品项ID列表")
    private List<String> orderItemIds;
}
