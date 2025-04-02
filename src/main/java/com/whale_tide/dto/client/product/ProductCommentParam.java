package com.whale_tide.dto.client.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 获取产品评论请求参数
 */
@Data
@ApiModel(value = "获取产品评论请求参数")
public class ProductCommentParam {
    private Long productId;
    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页记录数")
    private Integer pageSize;
}
