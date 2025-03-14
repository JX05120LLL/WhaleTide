package com.whale_tide.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdatePublishStatusParam {
    @ApiModelProperty("产品ID，多个用逗号分隔，必填")
    private String ids;

    @ApiModelProperty("上架状态：0->下架；1->上架，必填")
    private Integer publishStatus;

}
