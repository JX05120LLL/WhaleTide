package com.whale_tide.dto.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 资源返回结果
 */
@Data
public class ResourceResult {
    @ApiModelProperty("资源ID")
    private Long id;
    
    @ApiModelProperty("资源名称")
    private String name;
    
    @ApiModelProperty("资源URL")
    private String url;
    
    @ApiModelProperty("描述")
    private String description;
    
    @ApiModelProperty("资源分类ID")
    private Long categoryId;
    
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    @ApiModelProperty("是否被选中")
    private Boolean checked;
} 