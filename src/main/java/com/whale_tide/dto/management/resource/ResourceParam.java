package com.whale_tide.dto.management.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资源参数
 */
@Data
public class ResourceParam {
    @ApiModelProperty("资源名称")
    private String name;
    
    @ApiModelProperty("资源URL")
    private String url;
    
    @ApiModelProperty("描述")
    private String description;
    
    @ApiModelProperty("资源分类ID")
    private Long categoryId;
} 