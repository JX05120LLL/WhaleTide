package com.whale_tide.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 简单产品列表参数
 */
@Data
public class ProductSimpleResult {
    @ApiModelProperty("产品ID")
    public Long id;

    @ApiModelProperty("产品名称")
    public String name;
}
