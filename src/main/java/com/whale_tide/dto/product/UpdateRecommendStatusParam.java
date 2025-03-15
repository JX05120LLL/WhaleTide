package com.whale_tide.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateRecommendStatusParam {
    @ApiModelProperty("产品ID，多个用逗号分隔，必填")
    private String ids;

    @ApiModelProperty("推荐状态，0-不推荐，1-推荐，必填")
    private Integer recommendStatus;
}
