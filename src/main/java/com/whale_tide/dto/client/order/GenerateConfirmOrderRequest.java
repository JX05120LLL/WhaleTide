package com.whale_tide.dto.client.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 生成确认订单请求对象
 */
@Data
@ApiModel(description = "生成确认订单请求")
public class GenerateConfirmOrderRequest {
    
    @ApiModelProperty("购物车项ID列表")
    private List<Long> cartIds;
    
    @ApiModelProperty("收货地址ID")
    private Long addressId;
} 