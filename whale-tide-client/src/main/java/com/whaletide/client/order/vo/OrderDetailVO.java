package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情VO
 */
@Data
@Schema(description = "订单详情")
public class OrderDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

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
     * 支付方式
     */
    @Schema(description = "支付方式：1-支付宝；2-微信支付；3-银联支付")
    private Integer payType;

    /**
     * 支付方式文本
     */
    @Schema(description = "支付方式文本")
    private String payTypeText;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态：0-待付款；1-待发货；2-待收货；3-已完成；4-已取消；5-已关闭")
    private Integer status;

    /**
     * 订单状态文本
     */
    @Schema(description = "订单状态文本")
    private String statusText;

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
     * 运费金额
     */
    @Schema(description = "运费金额")
    private Double freightAmount;

    /**
     * 优惠金额
     */
    @Schema(description = "优惠金额")
    private Double discountAmount;

    /**
     * 支付时间
     */
    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    /**
     * 发货时间
     */
    @Schema(description = "发货时间")
    private LocalDateTime deliveryTime;

    /**
     * 收货时间
     */
    @Schema(description = "收货时间")
    private LocalDateTime receiveTime;

    /**
     * 订单备注
     */
    @Schema(description = "订单备注")
    private String note;

    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名")
    private String receiverName;

    /**
     * 收货人电话
     */
    @Schema(description = "收货人电话")
    private String receiverPhone;

    /**
     * 收货人地址
     */
    @Schema(description = "收货人地址")
    private String receiverAddress;

    /**
     * 订单项列表
     */
    @Schema(description = "订单项列表")
    private List<OrderItemVO> orderItems;
} 