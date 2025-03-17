package com.whale_tide.dto.management.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色参数
 */
@Data
public class RoleParam {
    @ApiModelProperty("角色名称")
    private String name;
    
    @ApiModelProperty("角色描述")
    private String description;
    
    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;
    
    @ApiModelProperty("排序")
    private Integer sort;
} 