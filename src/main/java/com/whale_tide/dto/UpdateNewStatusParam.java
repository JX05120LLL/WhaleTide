package com.whale_tide.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
public class UpdateNewStatusParam {
    @ApiModelProperty("产品ID，多个用逗号分隔，必填")
    private String ids;

    @ApiModelProperty("新品状态：0->不是新品；1->新品，必填")
    private Integer newStatus;
}
