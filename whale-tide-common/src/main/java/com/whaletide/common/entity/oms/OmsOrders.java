package com.whaletide.common.entity.oms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单信息
 */
@Data
@TableName("oms_orders")
public class OmsOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 订单总金额
     */
    private Double totalAmount;

    /**
     * 应付金额
     */
    private Double payAmount;

    /**
     * 运费金额
     */
    private Double freightAmount;

    /**
     * 优惠金额
     */
    private Double discountAmount;

    /**
     * 支付方式：1-支付宝，2-微信
     */
    private Integer payType;

    /**
     * 订单来源：1-APP，2-网页
     */
    private Integer sourceType;

    /**
     * 订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消，5-已关闭
     */
    private Integer status;

    /**
     * 订单类型：0-普通订单，1-秒杀订单
     */
    private Integer orderType;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 自动确认收货时间（天）
     */
    private Integer autoConfirmDay;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货人省份
     */
    private String receiverProvince;

    /**
     * 收货人城市
     */
    private String receiverCity;

    /**
     * 收货人区县
     */
    private String receiverRegion;

    /**
     * 收货人详细地址
     */
    private String receiverDetailAddress;

    /**
     * 订单备注
     */
    private String note;

    /**
     * 确认收货状态：0-未确认，1-已确认
     */
    private Integer confirmStatus;

    /**
     * 删除状态：0-未删除，1-已删除
     */
    private Integer deleteStatus;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;

    /**
     * 评价时间
     */
    private LocalDateTime commentTime;

    /**
     * 取消订单时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消订单原因
     */
    private String cancelReason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
