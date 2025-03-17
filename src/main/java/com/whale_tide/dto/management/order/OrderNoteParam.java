package com.whale_tide.dto.management.order;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新订单备注请求参数
 */

@Data
public class OrderNoteParam {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单备注")
    private String note;

    @ApiModelProperty("订单状态")
    private int status;

}
