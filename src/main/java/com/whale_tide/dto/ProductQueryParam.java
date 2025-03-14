package com.whale_tide.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品列表查询参数
 */
@Data
public class ProductQueryParam {
    @ApiModelProperty("搜索关键词")
    private String keyword;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页记录数")
    private Integer pageSize;

    @ApiModelProperty("上架状态")
    private Integer publishStatus;

    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("产品分类ID")
    private Long productCategoryId;
}
