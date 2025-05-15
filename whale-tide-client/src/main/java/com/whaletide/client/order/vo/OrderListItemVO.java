package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单列表项VO
 */
@Data
@Schema(description = "订单列表项")
public class OrderListItemVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long id;
    
    /**
     * 订单编号
     */
    @Schema(description = "订单编号")
    private String orderSn;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    /**
     * 订单总金额
     */
    @Schema(description = "订单总金额")
    private Double totalAmount;
    
    /**
     * 应付金额
     */
    @Schema(description = "应付金额")
    private Double payAmount;
    
    /**
     * 订单状态
     */
    @Schema(description = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;
    
    /**
     * 订单状态文本
     */
    @Schema(description = "订单状态文本")
    private String statusText;
    
    /**
     * 订单项列表
     */
    @Schema(description = "订单商品列表")
    private List<OrderItemVO> orderItems;
} 