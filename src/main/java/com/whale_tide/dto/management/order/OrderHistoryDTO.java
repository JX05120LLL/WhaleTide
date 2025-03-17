package com.whale_tide.dto.management.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel("订单历史记录DTO")
public class OrderHistoryDTO {
    @ApiModelProperty("ID")
    private Long id;
    
    @ApiModelProperty("订单ID")
    private Long orderId;
    
    @ApiModelProperty("操作人")
    private String operateMan;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("订单状态")
    private Integer orderStatus;
    
    @ApiModelProperty("备注")
    private String note;
} 