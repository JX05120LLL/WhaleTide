package com.whale_tide.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单列表查询结果
 */
@Data
public class OrderResult {
    @ApiModelProperty("订单ID")
    private Long id;
    
    @ApiModelProperty("订单编号")
    private String orderSn;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("会员用户名")
    private String memberUsername;
    
    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
    
    @ApiModelProperty("支付方式：0-未支付，1-支付宝，2-微信，3-银联")
    private Integer payType;
    
    @ApiModelProperty("订单来源：0-PC, 1-App, 2-小程序, 3-H5")
    private Integer sourceType;
    
    @ApiModelProperty("订单状态：0-待付款，1-待发货，2-已发货，3-已完成，4-已关闭，5-已取消")
    private Integer status;
    
    @ApiModelProperty("收货人姓名")
    private String receiverName;
    
    @ApiModelProperty("收货人电话")
    private String receiverPhone;
} 