package com.whale_tide.dto.management.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductCommentParam {
    private Long productId;
    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页记录数")
    private Integer pageSize;
}
