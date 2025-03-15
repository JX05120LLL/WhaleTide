package com.whale_tide.dto.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 菜单节点封装
 */
@Getter
@Setter
public class MenuNodeResult {
    @ApiModelProperty("菜单ID")
    private Long id;
    
    @ApiModelProperty("父菜单ID")
    private Long parentId;
    
    @ApiModelProperty("菜单名称")
    private String title;
    
    @ApiModelProperty("菜单级数")
    private Integer level;
    
    @ApiModelProperty("菜单排序")
    private Integer sort;
    
    @ApiModelProperty("菜单图标")
    private String icon;
    
    @ApiModelProperty("菜单是否选中")
    private Boolean checked;
    
    @ApiModelProperty("子菜单")
    private List<MenuNodeResult> children;
} 