package com.whale_tide.dto.client.product;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取品牌详情响应
 */

@Data
@ApiModel("获取品牌详情响应")
public class BrandDetailResponse {
    @ApiModelProperty("品牌ID")
    private Long id;
    @ApiModelProperty("品牌名称")
    private String name;
    @ApiModelProperty("品牌logo")
    private String logo;
    @ApiModelProperty("品牌大图")
    private String bigPic;
    @ApiModelProperty("商品数量")
    private Integer productCount;
    @ApiModelProperty("产品评论数量")
    private Integer productCommentCount;
    @ApiModelProperty("产品描述")
    private String description;
}
