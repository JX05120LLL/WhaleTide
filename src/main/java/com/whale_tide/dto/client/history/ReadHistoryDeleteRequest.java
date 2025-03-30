package com.whale_tide.dto.client.history;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 删除搜索历史记录请求参数
 */
@Data
@ApiModel("删除搜索历史记录请求参数")
public class ReadHistoryDeleteRequest {
    
    @NotEmpty(message = "ID列表不能为空")
    @ApiModelProperty(value = "历史记录ID列表", required = true)
    private List<Long> ids;
} 