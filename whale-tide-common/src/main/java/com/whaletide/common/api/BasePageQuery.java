package com.whaletide.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页查询基类
 */
@Data
public class BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @Schema(description = "每页数量", example = "10")
    @Min(value = 1, message = "每页数量不能小于1")
    private Integer pageSize = 10;
    
    @Schema(description = "排序字段")
    private String orderBy;
    
    @Schema(description = "排序方向", example = "desc")
    private String orderDirection;
} 