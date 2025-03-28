package com.whale_tide.dto.client.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 首页分类响应
 */
@Data
@ApiModel("首页分类响应")
public class HomeCategoryResponse {
    
    @ApiModelProperty("分类ID")
    private Long id;
    
    @ApiModelProperty("分类名称")
    private String name;
    
    @ApiModelProperty("分类图标")
    private String icon;
    
    @ApiModelProperty("分类级别")
    private Integer level;
    
    @ApiModelProperty("子分类列表")
    private List<HomeCategoryResponse> children;
    
} 