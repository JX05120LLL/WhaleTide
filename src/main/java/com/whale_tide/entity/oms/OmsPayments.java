package com.whale_tide.entity.oms;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付记录表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_payments")
public class OmsPayments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付流水号
     */
    private String paymentSn;

    /**
     * 支付方式：1-支付宝，2-微信，3-银联
     */
    private Integer paymentMethod;

    /**
     * 支付平台：0-PC，1-APP，2-小程序
     */
    private Integer paymentPlatform;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付状态：0-待支付，1-支付中，2-支付成功，3-支付失败
     */
    private Integer status;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 回调时间
     */
    private LocalDateTime callbackTime;

    /**
     * 回调内容
     */
    private String callbackContent;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
