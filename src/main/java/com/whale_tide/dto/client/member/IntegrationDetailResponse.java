package com.whale_tide.dto.client.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
/**
 * 获取用户积分详情响应参数
 */
@Data
@ApiModel(description = "获取用户积分详情响应参数")
public class IntegrationDetailResponse {
    @ApiModelProperty(value = " 当前积分")
    private Integer integration;
    @ApiModelProperty(value = " 历史总积分")
    private Integer historyIntegration;
    @ApiModelProperty(value = " 每1元获取的积分数量")
    private BigDecimal consumePerIntegration;
    @ApiModelProperty(value = " 最小使用单位")
    private Integer useIntegrationLimit;
}
