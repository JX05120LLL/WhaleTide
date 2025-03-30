package com.whale_tide.dto.client.history;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 搜索历史记录响应参数
 */
@Data
@ApiModel("搜索历史记录响应参数")
public class ReadHistoryResponse {
    
    @ApiModelProperty("搜索历史ID")
    private Long id;
    
    @ApiModelProperty("搜索关键词")
    private String keyword;
    
    @ApiModelProperty("搜索次数")
    private Integer searchCount;
    
    @ApiModelProperty("最后搜索时间")
    private Date lastSearchTime;
    
    @ApiModelProperty("创建时间")
    private Date createTime;
} 