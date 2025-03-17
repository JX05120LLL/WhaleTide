package com.whale_tide.dto.management.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单节点，用于构建树形菜单结构
 */
@Data
public class MenuNode {
    @ApiModelProperty("菜单ID")
    private Long id;
    
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
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("子菜单")
    private List<MenuNode> children;
} 