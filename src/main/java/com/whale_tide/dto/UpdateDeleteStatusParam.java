package com.whale_tide.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *更新删除状态参数
 */
@Data
public class UpdateDeleteStatusParam {
   @ApiModelProperty("产品ID，多个用逗号分隔，必填")
    private String ids;

    @ApiModelProperty("删除状态：0->未删除；1->已删除，必填")
    private Integer deleteStatus;

}
