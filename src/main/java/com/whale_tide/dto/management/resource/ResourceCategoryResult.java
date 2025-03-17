package com.whale_tide.dto.management.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 资源分类返回结果
 */
@Data
public class ResourceCategoryResult {
    @ApiModelProperty("资源分类ID")
    private Long id;
    
    @ApiModelProperty("资源分类名称")
    private String name;
    
    @ApiModelProperty("排序")
    private Integer sort;
    
    @ApiModelProperty("创建时间")
    private Date createTime;
} 