package com.whale_tide.dto.management.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品列表查询参数
 */
@Data
public class ProductQueryParam {
    @ApiModelProperty("搜索关键词")
    private String keyword;
    
    @ApiModelProperty("商品货号")
    private String productSn;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页记录数")
    private Integer pageSize;

    @ApiModelProperty("上架状态")
    private Integer publishStatus;
    
    @ApiModelProperty("审核状态")
    private Integer verifyStatus;

    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("产品分类ID")
    private Long productCategoryId;
}
