package com.whale_tide.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单参数，用于创建和更新菜单
 */
@Data
public class MenuParam {
    @ApiModelProperty("父级ID")
    private Long parentId;
    
    @ApiModelProperty("菜单名称")
    private String title;
    
    @ApiModelProperty("菜单级别")
    private Integer level;
    
    @ApiModelProperty("菜单排序")
    private Integer sort;
    
    @ApiModelProperty("前端图标")
    private String icon;
    
    @ApiModelProperty("前端路由路径")
    private String path;
    
    @ApiModelProperty("前端组件")
    private String component;
    
    @ApiModelProperty("重定向路径")
    private String redirect;
    
    @ApiModelProperty("是否隐藏：0-否，1-是")
    private Integer hidden;
    
    @ApiModelProperty("菜单状态：0-禁用，1-启用")
    private Integer status;
    
    @ApiModelProperty("菜单描述")
    private String description;
} 