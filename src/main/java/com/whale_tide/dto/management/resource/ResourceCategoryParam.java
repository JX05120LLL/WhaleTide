package com.whale_tide.dto.management.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资源分类参数
 */
@Data
public class ResourceCategoryParam {
    @ApiModelProperty("资源分类名称")
    private String name;
    
    @ApiModelProperty("排序")
    private Integer sort;
} 