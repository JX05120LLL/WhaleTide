package com.whale_tide.dto.management.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新收货人信息参数类
 */
@Data
public class ReceiverInfoParam {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("收货人姓名")
    private String receiverName;

    @ApiModelProperty("收货人手机号")
    private String receiverPhone;

    @ApiModelProperty("收货人邮编")
    private String receiverPostCode;

    @ApiModelProperty("收货人详细地址")
    private String receiverDetailAddress;

    @ApiModelProperty("收货人省份")
    private String receiverProvince;

    @ApiModelProperty("收货人城市")
    private String receiverCity;

    @ApiModelProperty("收货人区域")
    private String receiverRegion;

    @ApiModelProperty("订单状态")
    private Integer status;
}
