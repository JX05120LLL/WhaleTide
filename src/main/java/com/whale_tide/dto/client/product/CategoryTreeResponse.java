package com.whale_tide.dto.client.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品分类树响应
 */
@Data
@ApiModel(description = "商品分类树响应")
public class CategoryTreeResponse {

    @ApiModelProperty("商品ID")
    private Long id;
    @ApiModelProperty("商品名称")
    private String name;
    @ApiModelProperty("商品图片")
    private Integer level;
    @ApiModelProperty("子分类")
    private List<CategoryTreeResponse> children;

}
