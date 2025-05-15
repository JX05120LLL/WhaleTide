package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝查询结果VO
 */
@Data
@Schema(description = "支付宝查询结果")
public class AlipayQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long orderId;

    /**
     * 订单编号
     */
    @Schema(description = "订单编号")
    private String orderSn;

    /**
     * 交易号
     */
    @Schema(description = "交易号")
    private String tradeNo;

    /**
     * 交易状态：0-未支付，1-已支付
     */
    @Schema(description = "交易状态：0-未支付，1-已支付")
    private Integer tradeStatus;

    /**
     * 交易状态描述
     */
    @Schema(description = "交易状态描述")
    private String tradeStatusDesc;

    @Schema(description = "订单金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "买家实付金额")
    private BigDecimal buyerPayAmount;
    
    @Schema(description = "交易创建时间")
    private Date gmtCreate;
    
    @Schema(description = "交易付款时间")
    private Date gmtPayment;
} 