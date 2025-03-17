package com.whale_tide.dto.management.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 删除订单请求参数
 */
@Data
public class DeleteOrderParam {

    @ApiModelProperty("订单ID，多个用逗号分隔")
    private String ids;

}
