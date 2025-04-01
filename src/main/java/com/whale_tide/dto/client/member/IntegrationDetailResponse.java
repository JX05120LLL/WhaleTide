package com.whale_tide.dto.client.member;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel(description = "获取用户积分详情响应参数")
public class IntegrationDetailResponse {
    private Integer integration;
    private Integer historyIntegration;
    private BigDecimal consumePerIntegration;
    private Integer useIntegrationLimit;
}
