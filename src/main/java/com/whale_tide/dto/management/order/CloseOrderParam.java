package com.whale_tide.dto.management.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关闭订单参数
 */
@Data
public class CloseOrderParam {
    @ApiModelProperty("订单ID，多个用逗号分隔")
    private String ids;
    
    @ApiModelProperty("备注")
    private String note;
}
