package com.whale_tide.dto.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分配资源参数
 */
@Data
public class AllocResourceParam {
    @ApiModelProperty("角色ID")
    private Long roleId;
    
    @ApiModelProperty("资源ID列表")
    private List<Long> resourceIds;
} 