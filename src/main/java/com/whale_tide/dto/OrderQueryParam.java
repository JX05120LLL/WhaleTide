package com.whale_tide.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单查询参数
 */
@Data
public class OrderQueryParam {
    @ApiModelProperty("订单编号")
    private String orderSn;
    
    @ApiModelProperty("收货人关键词")
    private String receiverKeyword;
    
    @ApiModelProperty("订单状态：0-待付款，1-待发货，2-已发货，3-已完成，4-已关闭，5-已取消")
    private Integer status;
    
    @ApiModelProperty("订单类型：0-普通订单，1-秒杀订单，2-团购订单")
    private Integer orderType;
    
    @ApiModelProperty("订单来源：0-PC, 1-App, 2-小程序, 3-H5")
    private Integer sourceType;
    
    @ApiModelProperty("提交时间")
    private String createTime;
    
    @ApiModelProperty("页码")
    private Integer pageNum;
    
    @ApiModelProperty("每页记录数")
    private Integer pageSize;
} 