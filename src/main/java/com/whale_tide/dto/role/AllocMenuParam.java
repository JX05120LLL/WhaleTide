package com.whale_tide.dto.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分配菜单参数
 */
@Data
public class AllocMenuParam {
    @ApiModelProperty("角色ID")
    private Long roleId;
    
    @ApiModelProperty("菜单ID列表")
    private List<Long> menuIds;
} 