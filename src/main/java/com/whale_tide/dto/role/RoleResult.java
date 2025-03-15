package com.whale_tide.dto.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 角色返回结果
 */
@Data
public class RoleResult {
    @ApiModelProperty("角色ID")
    private Long id;
    
    @ApiModelProperty("角色名称")
    private String name;
    
    @ApiModelProperty("角色描述")
    private String description;
    
    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;
    
    @ApiModelProperty("排序")
    private Integer sort;
    
    @ApiModelProperty("创建时间")
    private Date createTime;
} 