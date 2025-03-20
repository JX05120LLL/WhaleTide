package com.whale_tide.dto.client.product;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 获取品牌列表请求,需要分页
 */

@Data
@ApiModel("获取品牌列表")
public class BrandListItemResponse  {
    @ApiModelProperty("品牌ID")
    private Long id;
    @ApiModelProperty("品牌名称")
    private String name;
    @ApiModelProperty("品牌logo")
    private String logo;
    @ApiModelProperty("商品数量")
    private Integer productCount;

}
